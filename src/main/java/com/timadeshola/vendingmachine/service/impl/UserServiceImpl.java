package com.timadeshola.vendingmachine.service.impl;

import com.timadeshola.vendingmachine.core.Translator;
import com.timadeshola.vendingmachine.core.exceptions.CustomException;
import com.timadeshola.vendingmachine.core.utils.AppUtil;
import com.timadeshola.vendingmachine.core.utils.ModelMapperUtil;
import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.model.request.UserRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.UserResponse;
import com.timadeshola.vendingmachine.persistence.entity.User;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import com.timadeshola.vendingmachine.service.ApplicationDao;
import com.timadeshola.vendingmachine.service.AuthenticationFacadeService;
import com.timadeshola.vendingmachine.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static com.timadeshola.vendingmachine.core.utils.AppUtil.verifyAmount;

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:07 PM
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ApplicationDao applicationDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationFacadeService facadeService;
    private final Translator translator;

    @Override
    public UserResponse createUser(UserRequest request) {
        userRepository.findByUsername(request.getUsername()).ifPresent(user -> {
            throw new CustomException(translator.toLocale("user.already.exist", request.getUsername()), HttpStatus.CONFLICT);
        });
        return ModelMapperUtil.map(userRepository.save(User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole().getRole())
                .build()), UserResponse.class);
    }

    @Override
    public UserResponse updateUser(UserRequest request, Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("user.not.found"), HttpStatus.NOT_FOUND);
        });
        if (request.getUsername() != null) {
            user.setUsername(request.getUsername());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole().getRole());
        }
        return ModelMapperUtil.map(userRepository.save(user), UserResponse.class);
    }

    @Override
    public Boolean deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("user.not.found"), HttpStatus.NOT_FOUND);
        });
        userRepository.delete(user);
        return true;
    }

    @Override
    public UserResponse fetchUser(Long id) {
        return ModelMapperUtil.map(userRepository.findById(id).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("user.not.found"), HttpStatus.NOT_FOUND);
        }), UserResponse.class);
    }

    @Override
    public PaginateResponse<UserResponse> fetchUser(int start,
                                                    int limit,
                                                    String startDate,
                                                    String endDate,
                                                    String username,
                                                    String role) {

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        Path<Timestamp> datePath = root.get("dateCreated");

        List<Predicate> predicates = new LinkedList<>();
        List<Order> orders = new ArrayList<>();

        if (!StringUtils.isEmpty(startDate)) {
            predicates.add(builder.greaterThanOrEqualTo(datePath, AppUtil.parseDateUtil(startDate)));
        }

        if (!StringUtils.isEmpty(endDate)) {
            predicates.add(builder.lessThanOrEqualTo(datePath,
                    DateUtils.addDays(Objects.requireNonNull(AppUtil.parseDateUtil(endDate)), 1)));
        }

        if (!StringUtils.isBlank(username)) {
            predicates.add(builder.like(root.get("username"), "%" + username + "%"));
        }

        if (!StringUtils.isBlank(role)) {
            predicates.add(builder.like(root.get("role"), "%" + role + "%"));
        }

        Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
        query.where(predicate);
        orders.add(builder.desc(root.get("dateCreated")));
        orders.add(builder.asc(root.get("username")));
        query.orderBy(orders);
        query.select(root);

        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        Long total = applicationDao.totalCount(builder, predicate, User.class);
        typedQuery.setFirstResult(start).setMaxResults(limit);

        return PaginateResponse.<UserResponse>builder()
                .content(ModelMapperUtil.mapAll(typedQuery.getResultList(), UserResponse.class))
                .totalElements(total).build();
    }

    @Override
    public BigDecimal deposit(BigDecimal amount) {
        User user = userRepository.findByUsername(facadeService.getPrincipal()).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("account.info.not.found"), HttpStatus.NOT_FOUND);
        });
        if (user.getRole().equals(RoleType.BUYER.getRole())) {
            if (verifyAmount(amount)) {
                user.setDeposit(user.getDeposit().add(amount));
                userRepository.save(user);
                return user.getDeposit();
            } else {
                throw new CustomException(translator.toLocale("deposit.error"), HttpStatus.PRECONDITION_FAILED);
            }
        }
        throw new CustomException(translator.toLocale("deposit.not.allowed"), HttpStatus.PRECONDITION_REQUIRED);
    }

    @Override
    public BigDecimal resetDeposit() {
        User user = userRepository.findByUsername(facadeService.getPrincipal()).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("account.info.not.found"), HttpStatus.NOT_FOUND);
        });
        user.setDeposit(BigDecimal.valueOf(0));
        user = userRepository.save(user);
        return user.getDeposit();
    }
}
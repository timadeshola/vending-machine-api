package com.timadeshola.vendingmachine.service.impl;

import com.timadeshola.vendingmachine.core.Translator;
import com.timadeshola.vendingmachine.core.exceptions.CustomException;
import com.timadeshola.vendingmachine.core.utils.AppUtil;
import com.timadeshola.vendingmachine.core.utils.ModelMapperUtil;
import com.timadeshola.vendingmachine.model.enums.RoleType;
import com.timadeshola.vendingmachine.model.request.ProductRequest;
import com.timadeshola.vendingmachine.model.response.PaginateResponse;
import com.timadeshola.vendingmachine.model.response.ProductPurchaseResponse;
import com.timadeshola.vendingmachine.model.response.ProductResponse;
import com.timadeshola.vendingmachine.persistence.entity.Product;
import com.timadeshola.vendingmachine.persistence.entity.User;
import com.timadeshola.vendingmachine.persistence.repository.ProductRepository;
import com.timadeshola.vendingmachine.persistence.repository.UserRepository;
import com.timadeshola.vendingmachine.service.ApplicationDao;
import com.timadeshola.vendingmachine.service.AuthenticationFacadeService;
import com.timadeshola.vendingmachine.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.http.HttpStatus;
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

/**
 * Project title: vending-machine
 *
 * @author johnadeshola
 * Date: 9/16/21
 * Time: 5:09 PM
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;
    private final ApplicationDao applicationDao;
    private final AuthenticationFacadeService facadeService;
    private final Translator translator;

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        User user = userRepository.findByUsername(facadeService.getPrincipal()).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("user.not.found"));
        });
        if (user.getRole().equals(RoleType.SELLER.getRole())) {
            productRepository.findByNameAndSeller_Id(request.getName(), user.getId()).ifPresent(product -> {
                throw new CustomException(translator.toLocale("product.already.exist", request.getName()), HttpStatus.CONFLICT);
            });
            return ModelMapperUtil.map(productRepository.save(Product.builder()
                    .name(request.getName())
                    .cost(request.getCost())
                    .seller(user)
                    .build()), ProductResponse.class);
        }
        throw new CustomException(translator.toLocale("product.access.denied"), HttpStatus.FORBIDDEN);
    }

    @Override
    public ProductResponse updateProduct(ProductRequest request, Long id) {
        User user = userRepository.findByUsername(facadeService.getPrincipal()).orElseThrow(() -> {
            throw new CustomException(translator.toLocale("user.not.found"));
        });
        if (user.getRole().equals(RoleType.SELLER.getRole())) {
            Product product = productRepository.findByIdAndSeller_Id(id, user.getId()).orElseThrow(() -> {
                throw new CustomException(translator.toLocale("product.not.found"), HttpStatus.CONFLICT);
            });
            if (request.getName() != null) {
                product.setName(request.getName());
            }
            if (request.getCost() != null) {
                product.setCost(request.getCost());
            }
            return ModelMapperUtil.map(productRepository.save(product), ProductResponse.class);
        }
        throw new CustomException(translator.toLocale("product.access.denied"), HttpStatus.FORBIDDEN);
    }

    @Override
    public Boolean deleteProduct(Long id) {
        if (facadeService.getAuthorities().contains(RoleType.SELLER.getRole())) {
            Product product = productRepository.findById(id).orElseThrow(() -> {
                throw new CustomException(translator.toLocale("product.not.found"), HttpStatus.NOT_FOUND);
            });
            if (facadeService.getPrincipal().equals(product.getSeller().getUsername())) {
                productRepository.delete(product);
                return true;
            }
        }
        throw new CustomException(translator.toLocale("product.delete.error"), HttpStatus.FORBIDDEN);
    }

    @Override
    public ProductResponse fetchProduct(Long id) {
        return ModelMapperUtil.map(productRepository.findById(id).<CustomException>orElseThrow(() -> {
            throw new CustomException(translator.toLocale("product.not.found"), HttpStatus.NOT_FOUND);
        }), ProductResponse.class);
    }

    @Override
    public PaginateResponse<ProductResponse> fetchProducts(int start,
                                                           int limit,
                                                           String startDate,
                                                           String endDate,
                                                           String sellerName) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);
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

        if (!StringUtils.isBlank(sellerName)) {
            predicates.add(builder.like(root.get("seller").get("username"), "%" + sellerName + "%"));
        }

        Predicate predicate = builder.and(predicates.toArray(new Predicate[]{}));
        query.where(predicate);
        orders.add(builder.desc(root.get("dateCreated")));
        orders.add(builder.asc(root.get("seller").get("username")));
        query.orderBy(orders);
        query.select(root);

        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        Long total = applicationDao.totalCount(builder, predicate, Product.class);
        typedQuery.setFirstResult(start).setMaxResults(limit);

        return PaginateResponse.<ProductResponse>builder()
                .content(ModelMapperUtil.mapAll(typedQuery.getResultList(), ProductResponse.class))
                .totalElements(total).build();
    }

    @Override
    public ProductPurchaseResponse purchaseProduct(Long productId) {
        User user = userRepository.findByUsername(facadeService.getPrincipal()).orElseThrow(() -> new CustomException(translator.toLocale("user.login.not.found"), HttpStatus.NOT_FOUND));
        if (user.getRole().equals(RoleType.BUYER.getRole())) {
            Product product = productRepository.findById(productId).orElseThrow(() -> new CustomException(translator.toLocale("product.no.longer.exist"), HttpStatus.NOT_FOUND));
            if (product.getCost().compareTo(user.getDeposit()) < 0) {
                BigDecimal accountBalance = user.getDeposit().subtract(product.getCost());
                user.setDeposit(accountBalance);
                product.setAmountAvailable(product.getAmountAvailable().add(product.getCost()));
                userRepository.save(user);
                productRepository.save(product);
                return ProductPurchaseResponse.builder()
                        .product(product.getName())
                        .change(AppUtil.resetAmount(accountBalance.intValue()))
                        .totalExpense(product.getCost())
                        .build();
            } else {
                throw new CustomException(translator.toLocale("insufficient.balance"), HttpStatus.EXPECTATION_FAILED);
            }
        }
        throw new CustomException(translator.toLocale("user.not.buyer"), HttpStatus.PRECONDITION_FAILED);
    }
}
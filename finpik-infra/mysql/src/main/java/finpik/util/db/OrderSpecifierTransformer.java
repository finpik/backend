package finpik.util.db;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.Expressions;
import finpik.error.enums.ErrorCode;
import finpik.error.exception.BusinessException;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.util.Assert;

public class OrderSpecifierTransformer {
    private final EntityPathBase<?> base;

    public OrderSpecifierTransformer(EntityPathBase<?> base) {
        Assert.notNull(base, "Base entity path must not be null");
        this.base = base;
    }

    public OrderSpecifier<?>[] transform(Sort sort) {
        return sort.stream()
            .map(this::toOrderSpecifier)
            .toArray(OrderSpecifier[]::new);
    }

    private OrderSpecifier<?> toOrderSpecifier(Sort.Order order) {
        Expression<Comparable<?>> expression = buildSortExpression(order);
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;
        OrderSpecifier.NullHandling nullHandling = mapNullHandling(order.getNullHandling());

        return new OrderSpecifier<>(direction, expression, nullHandling);
    }

    @SuppressWarnings("unchecked")
    private Expression<Comparable<?>> buildSortExpression(Sort.Order order) {
        PropertyPath path = resolvePropertyPath(order);

        Expression<?> expression = base;
        while (path != null) {
            expression = buildNextPath(expression, path, order.isIgnoreCase() && !path.hasNext());
            path = path.next();
        }

        try {
            return (Expression<Comparable<?>>) expression;
        } catch (ClassCastException e) {
            throw new BusinessException(ErrorCode.ORDER_BY_PROPERTY_NOT_FOUND);
        }
    }

    private PropertyPath resolvePropertyPath(Sort.Order order) {
        Assert.notNull(order, "Order must not be null");
        try {
            return PropertyPath.from(order.getProperty(), base.getType());
        } catch (PropertyReferenceException ex) {
            throw new BusinessException(ErrorCode.ORDER_BY_PROPERTY_NOT_FOUND);
        }
    }

    private Expression<?> buildNextPath(Expression<?> parent, PropertyPath path, boolean toLowerCase) {
        Path<?> parentPath = (Path<?>) parent;
        String segment = path.getSegment();
        Class<?> type = path.getType();

        if (toLowerCase) {
            return Expressions.stringPath(parentPath, segment).lower();
        }
        return Expressions.path(type, parentPath, segment);
    }

    private OrderSpecifier.NullHandling mapNullHandling(Sort.NullHandling nullHandling) {
        Assert.notNull(nullHandling, "NullHandling must not be null");
        return switch (nullHandling) {
            case NULLS_FIRST -> OrderSpecifier.NullHandling.NullsFirst;
            case NULLS_LAST -> OrderSpecifier.NullHandling.NullsLast;
            case NATIVE -> OrderSpecifier.NullHandling.Default;
        };
    }
}

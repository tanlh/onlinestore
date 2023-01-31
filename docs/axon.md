# Axon note

## Aggregate bean injection
We cannot use constructor injection for `@Aggregate` class. But, it allows to inject
via parameter of `@CommandHandler` method.

Refer: https://stackoverflow.com/questions/71264657/axon-framework-aggregate-autowired-bean-throws-nullpointerexception-in-test
```java
import com.photoapp.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;

@Aggregate
@Data
@NoArgsConstructor
public class ProductAggregate {

    @AggregateIdentifier
    String productId;
    String title;
    BigDecimal price;
    int quantity;

    @Autowired
    ProductMapper mapper; // => won't work, mapper is always null

    @CommandHandler
    public ProductAggregate(CreateProductCommand command, ProductMapper mapper) { // inject bean ProductMapper
        AggregateLifecycle.apply(mapper.toProductCreatedEvent(command));
    }
}
```

## Saga bean injection
The same to `@Aggregate`, constructor injection doesn't work with `@Saga` class. We are allowed to use field
and setter injection.

Refer: https://stackoverflow.com/questions/71265893/how-to-make-saga-starts

Moreover, we should use setter injection because field injection is not recommend. We can use some tricky lombok code
to reduce boilerplate setters. Read [lombok onX](https://projectlombok.org/features/experimental/onX)
```java
@Saga
@Slf4j
@Setter(onMethod_ = {@Autowired}) // there is an _ after onMethod, it's not mistake
public class OrderSaga {

    transient OrderMapper mapper;
    transient CommandGateway commandGateway;
    
    // some code

}
```

## Set based consistency validation
Read: [Set based consistency validation](https://developer.axoniq.io/w/set-based-consistency-validation)

## Common errors
### Google guava conflicts
We might get errors related to Google guava conflicts when starting application.
Explicitly defining the google guava version resolves the error.

### No handler for command
Axon will look up the handle method with the exact command class (full path include package). If Axon cannot find any handlers
for command, it will throw `No handler for ....` error.

In case you have command / event classes shared among multiple services, please make sure they are identical classes (same name and package).
There are some ways to do it:
- Create a shared module including shared command / event classes
- Use protobuf
- Duplicate the classes between services and put them into the same package

### Serialize error with record type
The default serializer of Axon framework doesn't support record type (Java 17). If your command / event classes
are record, you will get this error `can't get field offset on a record class`.

Luckily, jackson version 2.12+ supports record type and Axon framework support jackson. It can be done by this config:
```yaml
axon:
  serializer:
    events: jackson
    messages: jackson
```
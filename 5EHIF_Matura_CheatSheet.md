# 5EHIF Matura Cheat Sheet

# Practical Guide

## Persistence

- Annotate entities with `@Entity`. `@Table` may also be used, but is only needed, e.g. renaming table, configuring a multiple column unique constraint.
- Entities may extend `AbstractPersistable<?>` which implements a primary key field with the specified type. However, does not work with `@Inheritance`. In this case or when manually wanting to define a PK field use `@Id` and `@GeneratedValue` (exception: UUID, as UUID can be uniquely generated when creating entity)
- Embeddable Objects are annotated with `@Embedabble` and the field where they are used has the specific class as datatype with the `@Embedded` annotation.
- `@Column` may be used to enforce unique/nullable on a domain level.
- Enums require `@Enumerated` on the field where they are used. EnumType.String mostly, because ordinal breaks the enum if the order is changed.
- A list/set of enums/embedded objects additionally requires the `@ElementCollection` annotation.
- `@Inheritance` is used on the super class in inheritance. Inheritance-Strategies can be defined such as `InheritanceType.SINGLE_TABLE` (Roll-Up), `InheritanceType.TABLE_PER_CLASS` (Roll-Down), `InheritanceType.JOINED` (Combination, as common fields are in super-class table and specific fields in table of inherited classes). Inherited classes extend super class and `@SuperBuilder` may be used on super and also inherited classes so that a builder may be used.

### Relations

- `@OneToOne` one entity is connected to one other entity.
- `@OneToMany` one entity is linked to many entities, which means that the datatype can be a collection.
- `@ManyToOne` many entities of this type are linked to one entity of the other type, the type is the class of the related entity. `@OneToMany` can be used on the other side.
- `@ManyToMany` rarely used as an extra entity between the entities is used, which includes two `@OneToMany`

Bidirectional relations are relations which are defined on both sides. In a `@ManyToOne` / `@OneToMany` relation this can be difficult, but an add/remove method can simplify the interaction with a set/list. These methods simply take an object and either add or remove it from the collection. Saving/Persisting is important in bidirectional relations, as only saved entities should be added to the collection, so either relying on cascade types or a proper save order is important.

### Cascading

Cascading types can be used in relations, but are not required. However, they can be very beneficial, as they simplify operations between relations.

- `CascadeType.ALL` includes all the following CascadeTypes
- `CascadeType.PERSIST` used for handing over the save/persist operation, when base entity is saved, also the relationship-entity is saved.
- `CascadeType.MERGE` when saving the base entity, merges relationship entities with the same identifier, e.g. two relations with the same identifier get merged into one entity.
- `CascadeType.REMOVE` also deletes entities which have a relation with the base entity.
- `CascadeType.REFRESH` also rereads the child values from the database, when reading base entity.
- `CascadeType.DETACH` same as remove, but also deletes from persistence context.

### Creating Entity

Entity:
```java
public class EntityName extends AbstractPersistable<Long>
```
Annotations: `@Entity`, `@Data`, `@Builder`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@EqualsAndHashCode`

Fields:
```java
@Version
private Integer version;
```

### Fields

Enum:
```java
@Enumerated(EnumType.STRING)
````

Unique:
```java
@Column(unique = true, nullable = true)
```

Many-to-One (single object side)
```java
@ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
@JoinColumn(name = "address_id")
```

One-to-many (collection side) (`mappedBy` for bidirectional relationships)
```java
@OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE}, mappedBy = "person")
@Builder.Default
private List<Test> tests = new ArrayList<>();
```

Embeddable class (e.g. Address) above class:
```java
@Embeddable
```

Embedded field:
```java
@Embedded
```

Inheritance (single table with discriminator field) - besides `@MappedSuperclass`:
```java
@Entity(name="products")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="product_type",
        discriminatorType = DiscriminatorType.INTEGER)
public class MyProduct
```
```java
@Entity
@DiscriminatorValue("1")
public class Book extends MyProduct
```

Other inheritance strategies: `@Inheritance(strategy = InheritanceType.JOINED)` and `@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)` (allows polymorphic queries)

### Repositories

#### Definition
````java
public interface AddressRepository extends JpaRepository<Address, Long>
````

#### Queries
Returning collection:
```java
List<Person> findPeopleByAddress_CityContains(String text);
```

Returning single object:
```java
Optional<Person> findByPhoneNumber(PhoneNumber phoneNumber);
```

Custom query with annotation above interface method with custom name(`:name` is a method parameter which is injected):
```java
@Query("select u from User u where u.name like :name")
```

Pagination:
```java
// repo
List<Product> findAllByPrice(double price, Pageable pageable);

// Service
Pageable sortedByName = PageRequest.of(0, 3, Sort.by("name"));
```

Inheritance: getting child class for `SINGLE_TABLE` inheritance (`Person` is super class and `Employee` is child class):
```java
@Query("from Employee")
List<Employees> getAllEmployees();
```

Projections (via interfaces):
```java
public interface PersonView {
    String getLastName();
}
```
```java
public interface Repo ... {
    List<AddressView> getAddressByState(String state);
}
```

### Testing

#### Definitions
Repository test class:

```java
@DataJpaTest
public class PersonRepoTest 
```

Autowire tested repo(s):
```java
@Autowired
private PersonRepository personRepository;
```

Before each:
```java
@BeforeEach
void setup() {
    // add fixtures to repo(s)    
}
```

Annotate test methods:
```java
@Test
void ensureThatXYZWorks()
```

#### Test Structure
- **given** - test fixtures should be in repo(s)
- **when** - an operation is performed/query is executed
- **then** - *assertions*

#### Assertions
Example:
```java
assertThat(persons.size()).isEqualTo(2);
```

Import:
```java
import static org.assertj.core.api.Assertions.assertThat;
```

## Services

### Definitions

Class:
```java
@Service
@RequiredArgsConstructor
public class TestService
```

Injections:
```java
private final TestRepository testRepository;
```

Ensure method parameters (basic version):
```java
Objects.requireNonNull(test);
```

Transactions: Annotation placed on class- or method-level. Rollback for runtime, unchecked exceptions (= errors inside the program logic, e.g. ArithmeticException)
```
@Transactional
```

Use `@Transactional` to enable transactions on a certain method. If an error occurs, the changes get rolled-back, but if everything works, the changes get committed. Therefore, this annotation should be used when handling create-, update- or delete-operations, as only completely successful executions change the data.

### DTOs and Commands

Work with DTOs:
```java
@Data
@Builder
@AllArgsConstructor
public class SampleDto {}
```

Or with records:
```java
public record SampleDto (String name) {}
```

Command:
```java
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CreateEventCommand
```


### Custom exceptions

Class:
```java
@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class AlreadyExistsException extends RuntimeException
```

Constructor:
```java
private AlreadyExistsException(String msg) {
    super(msg);
}
```

> Important: Implement methods using factory pattern


### Extras

Age calculation:
```
ChronoUnit.YEARS.between(temporal, temporal);
```

Regex:
```java
Pattern pattern = Pattern.compile("\\bfox\\b"); // ^$ for whole string match
Matcher matcher = pattern.matcher(inputString);
matcher.find() // if true =>
String matchgroup = matcher.group();
inputString.matches("\\bfox\\b") // => returns boolean
```

| Code | Character Class                                |
|------|------------------------------------------------|
| .    | Any character (may or may not match terminator) |
| `\d`   | Any digits, `[0-9]`                              |
| `\D`   | Any non-digit, `[^0-9]`                          |
| `\s`   | Any whitespace character, `[\t\n\x0B\f\r]`       |
| `\S`   | Any non-whitespace character, `[^\s]`            |
| `\w`   | Any word character, `[a-zA-Z_0-9]`               |
| `\W`   | Any non-word character, `[^\w]`                  |
| `\b`   | A word boundary                                 |
| `\B`   | A non-word boundary                             |

### Streaming API

List => Stream => List:
```java
someList.stream().toList();
```

Map:
```java
someStream.map(oldObj -> newObj);
someStream.map(someFunction); // new Function<T,R>()
```

Double group-by:
```java
someStream.collect(Collectors.groupingBy(test -> test.getPerson().getGender(), 
        Collectors.groupingBy(test -> test.getPerson().getAgeRange())))
```

Flat map `List<List<SomeObj>>`:
```java
someStream.flatMap(Collection::stream).collect(Collectors.toList());
```

Map => Set => Stream:
```java
someMap.entrySet().stream()
```

Get max. value:
```java
someStream.max(Comparator.comparing(PersonAndSum::getSumOfHorsePower)).get()
```

Filter values:
```java
someStream.filter(i -> i%2==1);
someStream.filter(somePredicate); // new Predicate<T>()
```

Sort values:
```java
someStream.sorted() // for Integer, String
someStream.sorted(someComparator) // new Comparator<Integer>()
```

Reduce stream (e.g. sum):
```java
someStream.reduce(0, (a, b) -> a + b);
someStream.reduce(identity, binaryOperator);
// new BinaryOperator<Integer>()
```

Test condition on stream:
```java
someStream.allMatch(predicate);
someStream.anyMatch(predicate);
```

Collectors:
```java
someStringStream.collect(Collectors.joining(" ")); 
somestream.collect(Collectors.counting()); 
someStream.collect(Collectors.summarizingDouble(i -> i)); 
someStream.collect(Collectors.maxBy(Comparator.naturalOrder());
someStream.collect(Collectors.minBy(Comparator.naturalOrder()));
someStream.collect(Collectors.partitioningBy(s -> s.length() > 2))
```

### Testing

#### Definitions

Test class using mockito:
```java
@ExtendWith(MockitoExtension.class)
class TestServiceTest 
```

Mock repo(s) (or `@MockBean`):
```java
@Mock
private TestRepository testRepository;
```

Service class as field:
```java
private TestService testService;
```

Create service with mocked repository:
```java
@BeforeEach
void setup() {
    testService = new TestService(testRepository);
}
```

Annotate test methods:
```java
@Test
void ensureThatXYZWorks()
```

#### Mocking

Train your dependency objects for unit tests:
```java
when(eventRepository.findByToken(any())).thenReturn(Optional.of(event1));
```

Mocking void methods:
```java
doNothing().when(myMock).add(isA(Integer.class), isA(String.class));
myMock.add(0, ""); // void method
verify(myList, times(1)).add(0, ""); // verify
```

Mocking exceptions (for void methods):
```java
doThrow(IllegalStateException.class).when(myMock)
        .add(anyString(), anyString());
assertThrows(IllegalStateException.class, () -> {
    myMock.add("word", "meaning");
}); // verify
```

Mocking exceptions (for non-void methods):
```java
when(dictMock.getMeaning(anyString()))
        .thenThrow(NullPointerException.class);
assertThrows(NullPointerException.class, () -> {
    dictMock.getMeaning("word");
});
```

Test for empty return: `Optional.empty()`

#### Advanced assertions and verifications

Straightforward assertion:
```java
assertThat(newEvent.getToken()).isEqualTo(event1.getToken());
```

Assert that exception occurs:
```java
assertThrows(NotFoundException.class, () ->
        eventService.updateEvent("aNonExistentToken", newCreateEventCommand));
```

Assert class equivalence:
```java
assertThat(String.class).isEqualTo("dfsdf".getClass())
```

Verify invocations on dependency objects:
```java
verify(myMockService).findByToken("testToken1");
```

Verify that no other interactions occur:
```java
verifyNoMoreInteractions(myMockService);
```

## Presentation - API

Good practice mapping to **DTO**


### Annotations

- Annotate class with `@RestController`, `@RequestMapping("base-url")` & `@RequiredArgsConstructor` to enable spring injecting service classes
- Endpoints annotated with `GET`, `POST`, `PUT` (idempotent), `PATCH`, `DELETE`and then a path `("my-path/xyz")` --> Will now be accessible under `"/base-url/my-path/xyz"`
- `@PathVariable <type> <name>` for `"/{<name>}"` variables in path
- `@RequestBody <CommandClass> command` for JSON body
- `@RequestParam boolean include` to create a request param at the end of the path --> `"/base-url/my-path/id?include={true/false}"`

### Response Codes

Spring will interfer most Response Codes by default:
- **405** Bad Request if JSON is incorrect etc.
- **404** If the path does not match
- **500** If no specific error can be found

Can also set and return response codes manually by using e.g. `ResponseEntity.created(body).build()`

When sending `201 - Created` a location header is to be provided by using `URI`, which links where to access the created instance.

```
URI self = UriComponentsBuilder.fromPath(BASE_URL + "/{uuid}")
        	.uriVariables(Map.of("uuid", flow.getUuid()))
                .build().toUri();
```

### Creating Controller

Creating the class:

```java
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(PersonController.BASE_URL)
@RequiredArgsConstructor
public class PersonController
```

Field:
```java
public static final String BASE_URL = "/api/persons";
```

### HTTP GET (Fetch)
- `@GetMapping({ "/otherPath", "/{id}" })`
- return `ResponseEntity<SomeDto>` or sometimes `ResponseEntity<?>`
- Parameters:
   - `@PathVariable("id") Long personId`
   - `@RequestParam(name = "abc", required = false, defaultValue = "no") String include` => e.g. `/434?abc=yes`
- create response: `ResponseEntity.ok(someService.getEntity(id))`

### HTTP POST (Create)
- `@PostMapping({ "/otherPath", "/{id}" })`
- return `ResponseEntity<SomeDto>` or sometimes `ResponseEntity<?>`
- Parameters:
   - `@PathVariable("id") Long personId`
   - `@RequestParam(name = "abc", required = false, defaultValue = "no") String include` => e.g. `/434?abc=yes`
   - `@Valid @RequestBody CreateCommand command`
- create response: `ResponseEntity.created(link).body(dto)`

### HTTP PUT (Update)
- `@PostMapping({ "/otherPath", "/{id}" })`
- return `ResponseEntity<SomeDto>` or sometimes `ResponseEntity<?>`
- Parameters:
   - `@PathVariable("id") Long personId`
   - `@RequestParam(name = "abc", required = false, defaultValue = "no") String include` => e.g. `/434?abc=yes`
   - `@Valid @RequestBody UpdateCommand command`
- create response: `ResponseEntity.ok(someService.updateEntity(id))`

### HTTP DELETE
- `@DeleteMapping({ "/otherPath", "/{id}" })`
- return `ResponseEntity<SomeDto>` or sometimes `ResponseEntity<?>`
- Parameters:
   - `@PathVariable("id") Long personId`
   - `@RequestParam(name = "abc", required = false, defaultValue = "no") String include` => e.g. `/434?abc=yes`
- create response: `ResponseEntity.ok().build()`

### Attention
- Commands as parameters
- DTOs in responses
- self locations for POST and PUT operations

### Addons
Wrapping `List<DAO>` to `List<DTO>`:
```java
someService.getPeople().stream().map(PersonDto::of).toList()
```

Creating URIs:
```java
URI.create("%s/%s".formatted(path,id))
```

### Validation for Commands (Jakarta)

| Data Type | Validation Annotation | Description |
| --------- | --------------------- | ----------- |
| String | `@NotNull` | Validates that the annotated string is not null |
|          | `@NotBlank` | Validates that the annotated string is not null or blank |
|          | `@Size` | Validates that the annotated string's length is within a specified range |
|          | `@Pattern` | Validates that the annotated string matches a specified regular expression |
| Integer/Long | `@NotNull` | Validates that the annotated integer/long is not null |
|              | `@Min` | Validates that the annotated integer/long is greater than or equal to a specified value |
|              | `@Max` | Validates that the annotated integer/long is less than or equal to a specified value |
|              | `@Positive` | Validates that the annotated integer/long is positive |
|              | `@Negative` | Validates that the annotated integer/long is negative |
|              | `@PositiveOrZero` | Validates that the annotated integer/long is positive or zero |
|              | `@NegativeOrZero` | Validates that the annotated integer/long is negative or zero |

Validate ISO timestamp:
```java
@NotNull
@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd'T'HHmmss")
private LocalDateTime timeStamp;
```

### Testing (Controller)

#### Definitions

Class:
```java
@ExtendWith(SpringExtension.class)
public class ControllerTest
```

Create your mock(s):
```java
@MockBean
private TestService testService;
```

Specifying fields:
```java
private MockMvc mockMvc;
private final ObjectMapper mapper = new ObjectMapper();
```

Setup your mockMvc instance:
```java
@BeforeEach
void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(new Controller(testService)).build();
}
```

#### Writing a test

> **Note**: Commands should have the `@EqualsAndHashCode` annotation, for mocking behavior (equals method is called to check mock invocations)

1. Annotate test:
```java
@Test
void ensureXYZWorks()
```

2. Train your mocks (see above) => service invocations

3. Get JSON payload for request:
```java
String json = mapper.writeValueAsString(new SomeCommand("abc", 143));
```

4. Create request
```java
mockMvc.perform(post(endpointString).content(json)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
```

5. Assertions

Print results:
```java
.andDo(print())
```

Check status:
```java
.andExpect(status().isOk()) // .isCreated() .isBadRequest()
```

Content type:
```java
.andExpect(content().contentType(MediaType.APPLICATION_JSON))
```

JSON path:
```java
 .andExpect(jsonPath("$.id").value(testId))
```

JSON array:
```java
.andExpect(jsonPath("$[0].yearOfBirth").value(personStatistic.get(0).getYearOfBirth()))
```


### Testing - Annotations

Mockmvc to simulate HTTP requests, and Mockito to mock service layer

- Annotate Test Class with `@ExtendWith(MockitoExtension.class)`
- Create `@MockBean` of service class
- Create instance of MockMvc and assign in `@BeforeEach` using `MockMvcBuilders.standaloneSetup(new Controller(mockedService)).build();`

In `@Test`
1. use `mock()` to mock service
2. `mockMvc.perform(get("path").accept(MediaType.xyz))`, when using requests like `post()` also add `.content("jsonString")` and `.contentType()`
3. Test status code with e.g. `andExpect(status().isOk())`
4. Test JSON response with `andExpect(jsonPath().value(expected))`
   - `$` document root (e.g. `$.name`)
   - `@` current node (most likely not needed)
   - array access using e.g. `$.vehicles[0].xyz`

**Careful:** Lots of these methods have multiple imports. In general use `MockMvcRequestBuilders`, `MockMvcResultHandlers` & `MockMvcResultMatchers`. Easiest is to important everything from there by using `.*`
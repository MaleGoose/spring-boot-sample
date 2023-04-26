# 5EHIF Matura Cheat Sheet

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

### Testing

Test class with `@DataJpaTest` and `@Autowired` on repository instances.

## Service

- Use `@Transactional` to enable transactions on a certain method. If an error occurs, the changes get rolled-back, but if everything works, the changes get committed. Therefore, this annotation should be used when handling create-, update- or delete-operations, as only completely successful executions change the data. 

### Testing 

- Annotate Test Class with `@ExtendWith(SpringExtenstion.class)`
- Create `@MockBean` of repository 

## Presentation

Good practice mapping to **DTO**

### Annotations

- Annotate class with `@RestController`, `@RequestMapping("base-url")` & `@AllArgsConstructor` to enable spring injecting service classes
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

When sending `201 - Created` a location header is to be provided by using `URI`, which links where to access the created instance. `URI.create("basepath/myId")` then used as argument for `.created(uri)`. 

### Testing (Unit)

Mockmvc to simulate HTTP requests, and Mockito to mock service layer

- Annotate Test Class with `@ExtendWith(SpringExtenstion.class)`
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
# 5EHIF Matura Cheat Sheet

## Persistence

### Cascading

## Service

## Presentation

Good practice to map to Dtos

### Annotations

- Annotate class with `@RestController`, `@RequestMapping("base-url")` & `@AllArgsConstructor` to enable spring injecting service classes
- Endpoints annotated with `GET`, `POST`, `PUT`, `PATCH`, `DELETE`and then a path `("my-path/id")` --> Will now be accessible under `"/base-url/my-path/id"`
- `@RequestParam boolean include` to create a request param at the end of the path --> `"/base-url/my-path/id?include={true/false}"`

### Response Codes

Spring will interfer most Response Codes by default:
- **405** Bad Request if JSON is incorrect etc.
- **404** If the path does not match
- **500** If no specifc error can be found

Can also set and return response codes manually by using e.g. `ResponseEntity.created(body).build()`

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
   - `@` when response is an array
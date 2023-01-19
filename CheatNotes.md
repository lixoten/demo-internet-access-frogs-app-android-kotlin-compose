# My Cheat Notes

## Create Project
- Do any Upgrade needed

## in gradle(app)
- add plugin
    - id 'org.jetbrains.kotlin.plugin.serialization' version '1.7.20'
- add Dependencies
    - for the viewmodel
    - implementation "androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1"
    - for retrofit
        - implementation "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        - implementation "com.squareup.retrofit2:retrofit:2.9.0"
    - for json file
        - implementation "org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1"
    - for images in json
        - implementation "io.coil-kt:coil-compose:2.2.2"

## Package Structures
- Add these:
    - data
    - di
    - model
    - network
    - ui > screens

## In Manifest
we need:
- Above <application
    - `<uses-permission android:name="android.permission.INTERNET"/>`

## in ui create FrogsApp.kt
- ui > FrogsApp.kt - file @Composable
    - `fun FrogsApp(modifier: Modifier = Modifier) {`
    - Scaffold Args
        - topbar = {}
            - // todo FrogsTopAppBar()
    - Scaffold Body
        - Surface Args
            - modifier + it-padding... (get rid of error in code)
            - color to theme background
        - Surface Body
            - Text Composable with WTFhello
                - // todo Screens composables

## get rid of Greetings()
- Comment out/ remove Greeting() Composable
- Comment out/ remove DefaultPreview() Composable, we won't need it here
- remove Surface
- inside theme
    - FrogsApp()

## In ui create FrogsApp.kt
- in the Surface body
    - Create HomeScreen() Composable

### Run to make sure all is OK


## Model
- In model package
- Create file: FrogRec.kt - data class
    - `@Serializable data class FrogRec(`
    - list your fields
        - example
            - `val name: String`
            - `@SerialName(value = "img_src") val imgSrc: String,`
        - note: if json use `@SerialName()`
        - note: if gson use `@SerializedName()`
        - only needed if the field name coming in is different then what u use


# Network
- In network package
- Create file : FrogsApiService.kt - Interface class
    - `interface FrogsApiService {`
        - companion object with BASE_URL
            - this is the url from a "RESTful" server
        - `suspend fun getFrogsRecords(): List<FrogRec>`
            - Annotate with `@GET("amphibians")` // This is our EndPoint


## Repository
- Inside Data Package
- Create File : FrogsRepository.kt - Interface class
    - interface FrogsRepository {
        - `suspend fun getFrogsRecords(): List<FrogRec>`
- Use place cursor and Alt + Enter to implement FrogsRepository interface.. name it "DefaultFrogsRepository"
    - this will create DefaultFrogsRepository.kt
        - Add Args: `private val frogsApiService: FrogsApiService`
        - in override: `= frogsApiService.getFrogsRecords()`


## DI - AppContainer
- Inside DI Package
- Create file: AppContainer.kt - interface
    - `interface AppContainer {`
    - in body:
        - `val frogsApiService: FrogsApiService`
        - `val frogsRepository: FrogsRepository`
- Implement... see code below
- Use place cursor and Alt + Enter to implement AppContainer interface.. name it "DefaultAppContainer"
    - this will create DefaultAppContainer.kt
- REPLACE both overrides with
    - Also... json ot gson?
```kotlin
    @OptIn(ExperimentalSerializationApi::class)
    override val frogsApiService: FrogsApiService by lazy {
        Retrofit.Builder()
            .baseUrl(FrogsApiService.BASE_URL)
            //.addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create()
    }

    override val frogsRepository: FrogsRepository by lazy {
        DefaultFrogsRepository(frogsApiService)
    }
```


## Application
- Inside root
- create file: FrogsApplication.kt  - class : Application()
    - `class FrogsApplication: Application() {`
- see code below
```kotlin
class FrogsApplication : Application() {
    /** AppContainer instance used by the rest of classes to obtain dependencies */
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer()
    }
}
```


## In Manifest
- Below/inside <application (NOTE.. it might already be there autogen it)
    - Make sure FrogsApplication.kt exists before adding below
    - `android:name=".FrogsApplication"`


## UiState
- inside ui > screens >
- Note: here we define our 3 states/screens
- Create file: FrogsUiState - sealed interface
    - `sealed interface FrogsUiState {`
        - `data class Success(val FrogList: List<FrogRec>) : FrogsUiState`
        - `object Error : FrogsUiState`
        - `object Loading : FrogsUiState`

## ViewModel
- inside ui > screens >
- Create file: FrogsViewModel.kt - class : viewmodel()
    - `class FrogsViewModel(private val frogsRepository: FrogsRepository) : ViewModel() {`
```kotlin
    private val _uiState = MutableStateFlow<FrogsUiState>(FrogsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getFrogRecords()
    }

    fun getFrogRecords() {
        viewModelScope.launch {
            _uiState.value = FrogsUiState.Loading
            _uiState.value = try {
                FrogsUiState.Success(frogsRepository.getFrogsRecords())
            } catch (e: IOException) {
                FrogsUiState.Error
            } catch (e: HttpException) {
                FrogsUiState.Error
            }
        }
    }

    /**
     * Factory for FrogsViewModel] that takes FrogsRepository] as a dependency
     */
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as FrogsApplication)
                val frogsRepository = application.container.frogsRepository
                FrogsViewModel(frogsRepository = frogsRepository)
            }
        }
    }
```

## Pass in the ViewModel
- in MainActivity.kt
- Change to:
    - `val viewModel : FrogsViewModel = viewModel(factory = FrogsViewModel.Factory)`
    - `FrogsApp(viewModel)`
- in FrogsApp.kt
- Change to:
```kotlin
@Composable
fun FrogsApp(
    viewModel: FrogsViewModel,
    modifier: Modifier = Modifier
)
```  

----------------------------------------------------------------------
## HomeScreen
- Inside ui > screens >
- Create file: HomeScreen.kt - @Composable
- notes: Our HomeScreen is really just a container with a when statement
    - Depending on the state.value we display either LoadingScreen, ErrorScreen, or ListScreen
```kotlin
    val uiState = viewModel.uiState.collectAsState().value
    when(uiState){
        is FrogsUiState.Loading -> LoadingScreen(modifier)
        is FrogsUiState.Success -> ListScreen(frogList = uiState.frogList, modifier = modifier)
        is FrogsUiState.Error -> ErrorScreen(retryAction = retryAction, modifier)
    }
```
- I kept the ListScreen inside the HomeScreen. It would be ok as a separate file too
- ListScreen()
    - `private fun ListScreen(frogList: List<FrogRec>, modifier: Modifier = Modifier) {`
    - `private fun ListItem(frogRec: FrogRec, modifier: Modifier = Modifier) {`

## Other Screens
- Create a file: LoadingScreen.kt - @Composable
    - `fun LoadingScreen(modifier: Modifier = Modifier) {`
- Create a file: ErrorScreen.kt - @Composable
    - `fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {`
- Note: To manually test Error Screen
    - Set device to Airplane mode
    - Start App... A click on Retry should not load data... no connections
    - Turn off airplane mode... A click on Retry should load data
- Note: To manually Image no loaded
    - Load App
    - Set device to Airplane mode
    - Scroll down several images, u should see broken-image image
    - Turn off airplane mode
    - Scroll about, images should load

## TopAppBar
- Lets return to TopAppBar todo
- Notes: You can create right inside the Scaffold i like it as a separate file
- Create file: MyTopAppBar.kt - @Composable (not sure where to place it, so for now i placed it in root)
    - `fun MyTopAppBar() {`
- In Scaffold
    - `topBar = { MyTopAppBar() }`

# Beautiful a bit with colors in MaterialTheme
- In res > colors.xml
    - Comment out all colors.. we are not using
    - Add
        - `<color name="green_700">#FF087f23</color>`
- In res > themes.xml
    - Change the `statusBarColor` to green_700
- In ui > theme > Color.kt
    - Comment out all colors.. we are not using
    - Add
        - `val Green200 = Color(0xFF80e27e)`
        - `val Green500 = Color(0xFF4caf50)`
        - `val Green700 = Color(0xFF087f23)`
- In ui > theme > Theme.kt
    - Do this in Both the `DarkColorPalette` and `LightColorPalette`
        - Comment out all colors.. we are not using
        - Replace with
            - `primary = Green200,`
            - `primaryVariant = Green700,`
            - `secondary = Teal200`

## CleanUp Warnings
- Things like unused imports and so on
- The way i do it...open 1 file at a time and look at Problem Tab and fix warnings

## Check into Local Git
- Menu > VCS > Enable Version Control Integration
- In Commit Tab
    - Select all files
    - Type in some notes "Initial Commit"
    - Click Commit
        - Code will be analyzed. This is also a great place to look for Warnings and fix them
    - if no Errors
        - Once you are happy, Click 'Commit Anyways' 
[![](https://jitpack.io/v/tsuryo/ValidationEditText.svg)](https://jitpack.io/#tsuryo/ValidationEditText)

# ValidationEditText
Android library provides a simple way to validate input, and handles ui using ValidationEditText.

<img width="200" alt="ValidationEditText" src="https://user-images.githubusercontent.com/42518244/103216181-1b40a500-491e-11eb-8bd3-7e873e109be0.gif">

### Prerequisites
Android 5.0+ API 21+

## Where to
* [Getting familiar](#getting-familiar)
* [Usage](#usage)
* [Docs](https://tsuryo.github.io/ValidationEditText/)
* [Installing](#installing)

# Features

* Validate with built in patterns [ValidationType](#validationtype)
* Validate with your own custom regex [Custom pattern validation](#custom-pattern-validation).
* Customizable text size, text color.
* Customizable backgrounds - all layout, input background.
* Automatically showing/dismissing error on validation process.

# Getting familiar
Classes you should be familiar with:  
  * [ValidationEditText](https://github.com/tsuryo/ValidationEditText/blob/15985e54830991c3035be407d3829beaf4ceddcc/ValidationEditText/src/main/java/com/tsoft/validationedittext/views/ValidationEditText.java#L24) - View that handles validation ui,  
    showing error nicely with animations, hints, input, colors, etc.  
    Handles validation logic as individual ValidationEditText.
  * [EditTextValidator](https://github.com/tsuryo/ValidationEditText/blob/15985e54830991c3035be407d3829beaf4ceddcc/ValidationEditText/src/main/java/com/tsoft/validationedittext/utils/EditTextValidator.java#L19) - This class will handle all the validation process,  
    such as Parsing your xml, adding ValidationEditTexts,  
    executing the actual validation process and more.
  * [ValidationEditText attributes](#xml)

# Usage

## Simplest way to use
### Java
```Java
	mValidator = new EditTextValidator(this);
	mValidator.setLayout(this, R.layout.activity_main);

	button.setOnClickListener(new View.OnClickListener() {
	    @Override
	    public void onClick(View v) {
	        mValidator.validate();
	    }
	});

```
#### EditTextValidator(ValidationListener listener)
Implement this interface to get notified with the results.
```Java
    public interface ValidationListener {
        void onValidated();

        void onFailedToValidate();
    }
```

#### EditTextValidator#setLayout(Activity a, int layout)
Param a - Set the hosting Activity.  
Param layout - Set the layout contains ValidationEditText resourceId.

#### EditTextValidator#setLayout(View v, int layout)
Param v - Set the hosting View - for fragments use it in onViewCreated(View view, @Nullable Bundle savedInstanceState)  
Param layout - Set the layout contains ValidationEditText resourceId.

[EditTextValidator](https://github.com/tsuryo/ValidationEditText/blob/15985e54830991c3035be407d3829beaf4ceddcc/ValidationEditText/src/main/java/com/tsoft/validationedittext/utils/EditTextValidator.java#L19)


### XML
```XML
	<com.tsoft.validationedittext.views.ValidationEditText
	    android:fontFamily="@font/satum"
	    android:hint="@string/enter_email"
	    app:error_text="Enter valid email address"
	    app:input_background="@drawable/vet_bg"
	    app:pattern="EMAIL"
	    app:text_color="@color/colorAccent" />

	<com.tsoft.validationedittext.views.ValidationEditText
	    android:hint="@string/enter_password"
	    app:custom_pattern="@string/pass_regex"
	    app:error_text="Custom error, enter valid"
	    app:input_background="@drawable/vet_bg"/>

```
| Attribute             | Use to                                                  |
|-----------------------|---------------------------------------------------------|
| android:inputType     | use to set the inputType                                |
| android:fontFamily    | use to set the font                                     |
| android:hint          | use to set the hint text                                |
| android:text          | use to set the text                                     |
| android:textColorHint | use to set the hint color                               |
| android:textSize      | use to set the text size                                |
| android:background    | use to set the whole view background                    |
| input_background      | use to set the input background                         |
| text_color            | use to set the input text color                         |
| error_text            | use to set the error text to show on validation failure |
| et_text               | use to set the input text                               |
| pattern               | use to set the [ValidationType](#validationtype)                           |
| custom_pattern        | use to set custom regex pattern                         |

Access this attributes from Java code using ValidationEditText  
getters and setters - [ValidationEditText](https://github.com/tsuryo/ValidationEditText/blob/15985e54830991c3035be407d3829beaf4ceddcc/ValidationEditText/src/main/java/com/tsoft/validationedittext/views/ValidationEditText.java#L24)

## Other ways to use
* Handle ValidationEditText individually

```Java
   ValidationEditText vet = findViewById(R.id.vEt);
   vet.setListener(new ValidationEditText.Listener() {
       @Override
       public void onValidated() {
           
       }

       @Override
       public void onFailure(String msg) {

       }
   });
   vet.validate();
```
* Adding ValidationEditText manually to EditTextValidator
```Java
   /**
    * One at a time
    * */
   mValidator.addEditText(vet);
   /**
    * Adding using varargs
    * */
   mValidator.setEts(vet, vet1, vet2);
   /**
    * Adding a List<ValidationEditText>
    * */        
   mValidator.setEts(Arrays.asList(vet, vet1, vet2));
```

# ValidationType
| ValidationType| Checks        |
| -------------  |:-------------:|
| EMAIL          | Checks for valid email pattern |
| PASSWORD       | Checks for min 8 characters, one capital, one small letter + one special character|
| PHONE     | Checks for valid phone pattern      |
| WEB_URL          | Checks for valid web url pattern |
| IP_ADDRESS       | Checks for valid ip address pattern      |
| NAME| Checks for at least 2 characters, first capital letter      |
| FULL_NAME| Checks for min 2 characters, first capital letter -twice + space      |

# Custom pattern validation
To use your own regex pattern:  
  2 options:  
  * set custom_pattern in xml.
  ```XML
	<com.tsoft.validationedittext.views.ValidationEditText
	    android:hint="Enter password"
	    app:custom_pattern="(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}"
	    app:error_text="Custom error, enter valid"/>
  ```
  * set pattern in java
  ```Java
  ValidationEditText vet = findViewById(R.id.vEt);
  vet.setCustomPattern("(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}");
```

### Installing

Add the JitPack repository to your build file.
Add it in your root build.gradle at the end of repositories:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

Add the dependency
```
dependencies {
        implementation 'com.github.tsuryo:ValidationEditText:v1.0'
}
```

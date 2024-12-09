## Movie Facts Prompt services
### Description
This application is designed for obtaining interesting facts about movies.

### Controller description
Two methods for obtaining facts are employed:

1. MoviePromptServiceV1Controller - controller where I utilize OpenAI.
2. MoviePromptServiceV2Controller - controller where I utilize KernelSemantic.


In order to retrieve the desired fact, it is necessary to specify the title of the movie:
![image](https://github.com/user-attachments/assets/e80e1fb8-b2f6-49bf-9657-f1a8f0fd9620)

### Changes in prompt settings (PromptExecutionSettings)

According to the observation on changing the temperature parameter in PromptExecutionSettings, by setting the temperature to 0.1, for instance, the output is more precise and focused. If I make temperature higher, for instance 0.9 I receive morebroad, diverse and creative output.



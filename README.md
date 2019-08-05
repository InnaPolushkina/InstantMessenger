[![Codacy Badge](https://api.codacy.com/project/badge/Grade/77956da139ea49eebf20b6a00f4040b5)](https://www.codacy.com/app/InnaPolushkina/InstantMessenger?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=InnaPolushkina/InstantMessenger&amp;utm_campaign=Badge_Grade)
# For more Java information and examples, see
# https://docs.semaphoreci.com/article/85-language-java
version: v1.0
name: Hello Semaphore
agent:
  machine:
    type: e1-standard-2
    os_image: ubuntu1804
blocks:
  - name: Java example
    task:
      jobs:
        - name: Run java
          commands:
            - java --version
            - mvn -v
            # Uncomment the following line to pull your code,
            # then proceed by adding your custom commands:
            #- checkout
# InstantMessenger

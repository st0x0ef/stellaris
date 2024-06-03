# Adding Stellaris to your project

if you want to add Stellaris to your project, you can use gradle for it.

**Adding the repository**

```groovy
repositories {
    maven {
        name "Stellaris Maven"
        url "https://maven.odysseyus.fr/releases"
    }
}

```

**Adding Stellaris as dependency**

```groovy
dependencies {
  implementation "com.st0x0ef:stellaris-{modloader}-{minecraft_version}:{stellaris_version}"
}
```

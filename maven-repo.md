# Adding Stellaris to your project

if you want to add Stellaris to your project, you can use gradle for it.

**Adding the repository**

::: code-group

```groovy [gradle]
repositories {
    maven {
        name "Stellaris Maven"
        url "https://maven.odysseyus.fr/releases"
    }
}
```

```xml [maven]
<repository>
  <id>odysseyus-maven</id>
  <name>Odysseyus Maven</name>
  <url>https://maven.odysseyus.fr/<repository></url>
</repository>
```

:::



**Adding Stellaris as dependency**

::: code-group

```groovy [gradle]
dependencies {
  implementation "com.st0x0ef:stellaris-{modloader}-{minecraft_version}:{version}"
}

```

```xml [maven]
<dependency>
  <groupId>com.st0x0ef</groupId>
  <artifactId>stellaris-{modloader}-{minecraft_version}</artifactId>
  <version>{stellaris_version}</version>
</dependency>

```

:::

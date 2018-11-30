# ${projectDescription}

## Description

Documentation project, using **AsciiDoctor** and **PlantUML**.
The document sources are in `src/docs`, and support generation of multiple documents.

The documents themselves us **document fragments** (`src/docs/includes/**/_{fragment-name}.adoc`) and can be shared among the main documents (make sure you prepend the underscore in the fragment file name, to prevent it from being rendered as a standalone document).

## Build

This project can be built with either **Gradle** or **Maven**.

### with Gradle (default tasks: _clean build_):

    gradle
	
The documents will be created in `build/docs/html5` and `build/docs/pdf`.

### with Maven (default tasks: _clean install_):

    mvn
    
The documents will be created in `target/docs/html5` and `target/docs/pdf`.

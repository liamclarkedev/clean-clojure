# clean-clojure

An example Clojure application using Clean Architecture.

- Dependency Injection - [Component](https://github.com/stuartsierra/component).
- Web - [Ring](https://github.com/ring-clojure/ring) and [Compojure](https://github.com/weavejester/compojure) using
  HTTP.
- Data - [OKSQL](https://github.com/swlkr/oksql) using Postgres.
- Logging - [tools.logging](https://github.com/clojure/tools.logging) using standard output.

## Usage

### Run Locally

Start the docker services to run a Postgres container.

```shell
docker compose up -d
```

Start the application.

```shell
lein run
```

Visit the generated OpenAPI documentation.

```shell
http://localhost:3000/docs
```

### Run tests

Tests are run using Leiningen.

```shell
lein test
```

Coverage report can be found in `target/coverage` after running the following command:

```shell
lein cloverage
```

## Use as a template

1. Find and replace `todo` for the example structure.
2. Final and replace `clean-clojure` for the app name.
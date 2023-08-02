# clean-clojure

[![CircleCI](https://dl.circleci.com/status-badge/img/gh/liamclarkedev/clean-clojure/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/liamclarkedev/clean-clojure/tree/main)
[![codecov](https://codecov.io/gh/liamclarkedev/clean-clojure/branch/main/graph/badge.svg?token=31C1DLQ2A4)](https://codecov.io/gh/liamclarkedev/clean-clojure)

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

1. Find and replace `todo` for the example feature.
2. Final and replace `clean-clojure` for the app name.

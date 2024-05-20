# project-link-lake-api

## Introduction

LinkLake provides a web API to manage **links** and **tags**.
It serves as an alternative to traditional browser bookmark management,
where bookmarks are typically saved into a directory.

In LinkLake, saved links (also known as bookmarks) can be tagged for simple retrieval.
This eliminates the need for users to remember the specific directory in which they saved a desired bookmark.

## Development Setup

The application uses [PostgreSQL](https://www.postgresql.org/) as primary data storage.
Under the `dokcer` directory there is a `docker-compose.yaml` for convenient database setup for development purpose.
It uses the following images:
* [postgres](https://hub.docker.com/_/postgres) - an official image for the database
* [dpage/pgadmin4](https://hub.docker.com/r/dpage/pgadmin4) - containerised version of PgAdmin tool

## Environment Variables Configuration

Before starting up the database, make sure to configure the following environment variables
referenced by the docker-compose definition. For example:

```shell
export DB_USERNAME=my_username
export DB_PASSWORD=my_password

export PGADMIN_EMAIL=admin@example.com
export PGADMIN_PASSWORD=my_password
```

Note that the application also references the `DB_USERNAME` and `DB_PASSWORD` environment variables
to connect to the database.
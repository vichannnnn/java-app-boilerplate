.ONESHELL:
SHELL = bash

backend_container := backend
docker_run := docker compose run --rm
docker_backend := $(docker_run) $(backend_container)
docker_compose_production_run := docker compose -f docker-compose.prod.yml run --rm $(backend_container)
docker_compose_dev_run := docker compose -f docker-compose.dev.yml run --rm $(backend_container)

-include ./Makefile.properties

hello:
	echo "Hello, world!"

init:
	mvn clean install -DskipTests=false

build:
	docker compose up -d --build

run:
	mvn spring-boot:run

install:
	mvn clean install

start: install \
	build \

dev: 
	docker compose up -d --build db
	mvn clean install
	mvn spring-boot:run

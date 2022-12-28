#!/bin/bash
docker run --name analysis \
 -e POSTGRES_PASSWORD=password \
 -e POSTGRES_USER=latusikl \
 -e POSTGRES_DB=postgres \
 -p 5432:5432 \
 -d postgres

sleep 5
PGPASSWORD=password psql -h localhost -U latusikl -d postgres -f schema.sql

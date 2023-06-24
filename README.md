# Backend-Auth
[![Build Flow](https://github.com/HRV-Mart/Backend-Auth/actions/workflows/build.yml/badge.svg)](https://github.com/HRV-Mart/Backend-Auth/actions/workflows/build.yml)
![Docker Pulls](https://img.shields.io/docker/pulls/harsh3305/hrv-mart-backend-auth)
![Docker Image Size (latest by date)](https://img.shields.io/docker/image-size/harsh3305/hrv-mart-backend-auth)
![Docker Image Version (latest by date)](https://img.shields.io/docker/v/harsh3305/hrv-mart-backend-auth)
## Set up application locally
```
git clone https://github.com/HRV-Mart/Backend-Auth.git
gradle clean build
```
## Set up application using Docker
```
docker run  --name HRV-Mart-Backend-Auth -it --nev APPWRITE_PROJECT_ID=PROJECT_ID --env APPWRITE_APIKEY=--init --net="host" -d harsh3305/hrv-mart-backend-auth:latest
```

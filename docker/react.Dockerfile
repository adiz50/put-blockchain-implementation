FROM node:18-alpine as builder

COPY frontend/ /frontend

WORKDIR ./frontend

RUN mkdir -p ./node_modules
RUN npm run build

#Build nginx image
FROM nginx:1.25.3-alpine

WORKDIR /usr/share/nginx/html

RUN rm -rf ./*

COPY --from=builder /frontend/build .
COPY nginx.conf /etc/nginx
WORKDIR ./
RUN mkdir "certs"
COPY certs/localhost.crt /certs/
COPY certs/localhost.key /certs/
ENTRYPOINT ["nginx", "-g", "daemon off;"]


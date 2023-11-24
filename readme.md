<h1>How to install:</h1>

1. docker network create exapp
2. docker run --name database --network=exapp -p 5432:5432 -e POSTGRES_DB=blockchain -e POSTGRES_PASSWORD=postgres -d adiz50/put-blockchain-implementation-db
3. docker run --name frontend --network=exapp -p 443:443 -p 80:80 -d adiz50/put-blockchain-implementation-frontend
4. docker run --name backend --network=exapp -p 8080:8080 -d adiz50/put-blockchain-implementation-backend

<h1>How to use</h1>
Go to site <a href="https://localhost">https://localhost</a><br>
List of open ports:<br>
    - 5432 - PostgreSQL database<br>
    - 8080 - backend<br>
    - 443 - HTTPS<br>
    - 80 - HTTP<br>

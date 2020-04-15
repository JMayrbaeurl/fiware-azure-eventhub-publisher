docker network create fiware_default

docker run -d --name=mongo-db --network=fiware_default --expose=27017 -v C:\Dev\docker\mounts\mongodb\fiwaredata:/data mongo:3.6 --bind_ip_all --smallfiles

docker run -d --name fiware-orion -h orion --network=fiware_default -p 1026:1026  fiware/orion -dbhost mongo-db
docker run -d --name fiware-publisher -h publisher --network=fiware_default -p 8080:8080 --env fiware.broker.url=http://fiware-orion:1026/v2/ --env FIWARE_EHNS_CONNSTRING=[Event Hub connection string]  jmayrbaeurl/fiware-publisher
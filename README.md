# Audio Handler App

## How to run
- Mandatory: install `docker`
- Kindly run `start_app.sh` example: `./start_app.sh`
- Wait until the service started by checking `http://localhost:8080/actuator/health`, if service has started it will return `{"status":"UP"}`

## Stack

---

Audio Handler App run with docker. The tech stack that I use for this development are: 

- **Java 21 with Spring boot framework**: to ease REST API development
- MySql: For storing audio file metadata such as user id, phrase id, and file path, although in my opinion here, we can avoid storing file path, because we can just create folder per use inside S3 bucket, and use phrase id as the file name.
- S3 bucket using localstack: simulate blob/object storage using S3, with aws sdk to store wav audio file.

## System design
 
---

For this coding challenge development with minimal design, here I only developed the simple design. The design contain A web service, connected to mysql and S3 bucket. 

This design can be improved if we want to use this for production environment. Things that I will improve for production release:

- Use async processing to convert and store audio file to S3, we can use message queue to process convert and store it again to S3.
- Handle REST API with authentication/api token or any
- Handle file validation properly
- Create Multiple configuration for each env
- Use load balancer for horizontal scaling
- Might need to separate audio convert and store service to avoid single point of failure, even retry mechanism if anything happened with audio conversion



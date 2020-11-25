# Microservieces - Example App designed as microservices architecture.


## Project Requirements
- Java 8+ version
- Spring 2.3.5 or higher 

## Project - Schema
![](https://github.com/rogu3451/MicroservicesExampleApp/blob/main/microservices-schema.jpg)

## Getting started

1. Clone repo (there are five directories:  Courses, Eureka-Server, Gateway, Notifications, Students )
	each directory represents separated microservice except for Eureka-Server and Gateway.
	
2. First open and run Eureka-Server. (Default URL for Eureka-Server is http://localhost:8761/)
   You should chceck if Eureka-Server works before going to the next steps.
   
3. Open and run Gateway which receive and manage requests. Gateway guarantee also higher security.
   The Gateway register on port 9000
   There are two main endpoints redirecting to STUDENT-SERVCIE and COURSE-SERVICE:
   http://localhost:9000/api/students/**
   http://localhost:9000/api/courses/**
   
4. Open and run simultaneously other remaining application like Courses, Students, Notifications and
   fill in application.properties file for each application. There are settings involve database address, name or password and address for instance of RabbitMQ.

5. Students Application uses postgreSql database and register in Eureka-Server as STUDENT-SERVICE
   Students Application - Endpoints - Port: 8080
	
	GET
	
	1.Returns all students with status equals ACTIVE.
	http://localhost:8080/students   
	BY Gateway API ->  http://localhost:9000/api/students
		
	2.Returns all students with specific status.
	http://localhost:8080/students?status=INACTIVE   
	BY Gateway API ->  http://localhost:9000/api/students?status=ACTIVE  
		
	3.Returns student with status ACTIVE by specific id given as request param.
	http://localhost:8080/students/{id}   
	BY Gateway API ->  http://localhost:9000/api/students/{id}
		
	4.Returns student with specific email.
	http://localhost:8080/students/email/{email}  
	BY Gateway API ->  http://localhost:9000/students/email/{email} 
	
	
	POST
	1.Returns particular list of students defined by specific list of emails given as a body of request.
	http://localhost:8080/students/emails   
	BY Gateway API ->  http://localhost:9000/students/emails
	
	2.Adds new student to database.
	http://localhost:8080/students  
	BY Gateway API ->  http://localhost:9000/students
		
	DELETE
	1.Changes status on INACTIVE (instead of permament deleting form database) 
	for specific student defined by particular id in URL path.
	http://localhost:8080/students/{id}   
	BY Gateway API ->  http://localhost:9000/api/students/{id}
		
	PUT
	1.Updates whole informations about student. 
	(If some informations are not specified, there are set as a null).
	http://localhost:8080/students/{id}   
	BY Gateway API ->  http://localhost:9000/api/students/{id}
	
	PATCH
	1.Updates (only specified by request)informations about student.
	http://localhost:8080/students/{id}   
	BY Gateway API ->  http://localhost:9000/api/students/{id}
		
6. Courses Application uses MongoDB database and register in Eureka-Server as COURSE-SERVICE
   The application uses Feign Client to communicate with STUDENT-SERVICE and RabbitMQ for sending notifications 
   about finish enrollment for specific course.
   Courses Application - Endpoints - Port: 8087
	
	GET
		
	1.Returns all courses from database.
	http://localhost:8087/courses   
	BY Gateway API ->  http://localhost:9000/api/courses
		
	2.Returns all courses with specific status.
	http://localhost:8087/courses?status=INACTIVE   
	BY Gateway API ->  http://localhost:9000/api/courses?status=ACTIVE  
		
	3.Returns courses with by specific id in this case called code.
	http://localhost:8087/courses/{code}   
	BY Gateway API ->  http://localhost:9000/api/courses/{code} 
		
	4.Returns course's members for specific course defined by particular code.
	http://localhost:8087/{courseCode}/members   
	BY Gateway API ->  http://localhost:9000/api/{courseCode}/members
		
	POST
	1.Adds new course to database
	http://localhost:8087/courses   
	BY Gateway API ->  http://localhost:9000/api/courses
		
	2.The endpoint allows enroll specific student on particular course.
	http://localhost:8087/courses/{courseCode}/student/{studentId}   
	BY Gateway API ->  http://localhost:9000/api/courses/{courseCode}/student/{studentId}
		
7. Notifications Application connects with RabbitMQ and receive notifications from queue by Rabbit Listener. 
   New notifications about finish enrollment for specific course are sending to particular students's emails.
   The app register in Eureka-Server as NOTIFICATION-SERVICE
   Notifications Application - Endpoints - Port: 8099
   
   1.There is one test POST endpoint:
   http://localhost:8099/email  
   
   Example JSON format:
   {
	"to":"reciver@gmail.com",
	"title":"Test Email form Notifications Service",
	"content":"Content of message... "
   }

# Getting Started


# REST API Course Enrollment Application

This application exposes APIs to create student identities by school authorities, and students will be able to enroll themselves in classes before each term.

## Pre-requisites

Git ([https://git-scm.com/book/en/v2/Getting-Started-Installing-Git](https://git-scm.com/book/en/v2/Getting-Started-Installing-Git))

Docker:([https://docs.docker.com/get-docker/](https://docs.docker.com/get-docker/))


## Technologies

- **Backend**
    - Spring Boot
    - Postgres DB
    - Gradle
    - & Libraries [Lombok, Map Struct, Mockito, Junit 5 and Swagger Open Api]
    - Spring Cache used for semester and courses.
- **DevOps**
    - Docker, AWS (Code PipeLine, ECR and ECS - (EC2))
    - Jacoco (Code Coverage)

## Project Setup

1. Go to the Terminal/Command line and clone this repository: ```git clone https://github.com/rajneesh17/course-enrollment-api```
2. cd course-enrollment-api
3. Build the application `./gradlew clean build`

##### Then create the build with docker compose to build docker image using built jar file

```docker-compose build```
### Then use following command to run whole setup using docker compose.

```docker-compose up```

#### Stop the application using below command
```docker-compose down```

With volumes.

```docker-compose down -v```

### To view the application logs
```docker logs course-enrollment-api```

The app will start running at <http://localhost:8080>

### API Documentation

To check the API documentation, which is automatically generated using Open API (Swagger).


Go to : [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)


## Explore Rest APIs

### APIs:

#### Steps to use this application first time using postman or any rest client tools

##### Step 1: Add a new semester
	Method: Post
	Url: http://localhost:8080/semesters
	Body [Json] : 
		{
		  "name": "Summer2022",
		  "startDate": "2022-05-02",
		  "endDate": "2022-05-02"
		}

#### Step 2: Add a new course
	Method: Post
	Url: http://localhost:8080/classes
	Body [Json] : 
		{
		  "name": "classA",
		  "credit": 4
		}

#### Step 3: Add a new student
		Method: Post
		Url: http://localhost:8080/students
		Body [Json] : 
		{
		 "id": 1,
		  "firstName": "John",
		  "lastName": "Griffen",
		  "nationality": "US",
		}
#### Step 4: Enroll student into a course for a particular semester
		Method: Post
		Url: http://localhost:8080/enrollments
		Body [Json] :
			{
			  "semester": "Summer2022",
			  "id": 1,
			  "course": "classA"
			}

#### Step 5: Get a student by id
		Method: Get
		Url: http://localhost:8080/students/1

### Other APIs
#### API to modify students
		Method: PUT
		Url: http://localhost:8080/students
		Body [Json] : 
		{
		 "id": 1,
		  "firstName": "John",
		  "lastName": "Griffen",
		  "nationality": "US",
		}

#### API to get the list of classes for a particular student for a semester or the full history of classes enrolled.
		Method: GET
		Urls:
			http://localhost:8080/fetchCourses?semester=Summer2022&course=
			
			http://localhost:8080/fetchCourses?id=1

#### API to get the list of students enrolled in a course for a particular semester [pagination enabled]
		Method: GET
		Urls:
			http://localhost:8080/fetchStudents
			
			http://localhost:8080/fetchCourses?id=1
			
			http://localhost:8080/fetchCourses?semester=Summer2022
			
			http://localhost:8080/fetchCourses?pageNo=0&pageSize=100

#### API to drop a student from a course.
		Method: Delete
		Url: http://localhost:8080/enrollments?id=1&course=classA

Note: ***It is recommended to use [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) for detailed information.***

#### Continuous deployment of above Rest APIs

The Git webhook can initiate the AWS CodePipeline automatically.

***buildSpec.yml*** helps to do the following

- Execute the gradle test and build
- Create a docker image and push it to ECR
- Create an imagedefinitions.json file for the deployment

The code pipeline for this application consists of three stages:
- Code Source - Pull from GitHub
- Code Build - Gradle build and Create a docker Image
- Code Deploy - Deploy into ECS - Ec2 instance


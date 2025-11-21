# 몽고디비 준비
어플리케이션 개발시 사용할 몽고디비를 준비하는 방법은 몽고디비에서 제공하는 클라우드 서비스를 이용하는 것과 직접 컴퓨터에 몽고디비를 설치하는 방법이 있습니다.
몽고디비 클라우드에서는 500MB 용량의 무료 데이터베이스 공간을 제공하므로 이것을 사용할 수 있습니다.
또한 도커허브에 몽고디비 이미지들이 등록되어 있으므로 도커가 설치된 컴퓨터라면 쉽고 빠르게 컴퓨터에서 몽고디비를 실행할 수도 있습니다.

# MongoDB Atlas
Create an account on https://cloud.mongodb.com/ and select free-tier

# MongoDB Compass
클라우드 서비스를 이용한다면 클라우드 콘솔을 사용하여 데이터베이스에 접근할 수 있지만 별도의 클라이언트 어플리케이션을 사용할 수도 있습니다.
콤파스는 몽고디비의 GUI 클라이언트로 플랫폼에 맞추어 다운로드 받아 실행할 수 있습니다.
[MongoDB Compass](https://www.mongodb.com/try/download/compass)
프로그램은 설치가 필요없는 단독 실행 가능한 exe 파일이며, 실행한 후에 연결할 몽고디비를 추가할 수 있습니다.

## MongoDB vs. ElasticSearch
MongoDB에서도 텍스트 검색을 지원하지만 전문 검색 엔진이 필요하다면 엘라스틱서치를 사용하면 됩니다.
엘라스틱 서치는 한국어 지원은 물론 유사어 검색등 다양하고 전문적인 검색을 할 수 있습니다.

## references
[Spring Data MongoDB repository](https://docs.spring.io/spring-data/mongodb/reference/repositories.html)
[텍스트 인덱스 언어](https://www.mongodb.com/ko-kr/docs/manual/reference/text-search-languages/)

## 몽고DB 기본 명령어
```declarative
데이터베이스 생성, 삭제 in shell
cls
show dbs
use admin (데이터베이스 이름)
use school (없으면 생성됨)
db.createCollection(“students”)
show dbs (이제야  school이 보인다. 컬렉션이 하나라도 있어야 함)
db.dropDatabase() - 현재 선택된 데이터베이스 삭제
show dbs (데이터베이스가 삭제된 것을 볼 수 있다.)

use school
db.students.insertOne({ name: “Spongeboob”, age: 30, gps: 3.2})
db.students.find() // select all
db.students.insertMany([{  }, {  }, {  }]) // document 형식이 모두 같을 필요는 없다.

db.students.insertOne({
name: “홍길동”,
age: 32,
gpa: 2.8,
fullTime: false,
registeredDate: new Date() // new Date(“2023-01-10T00:00:00”,
graduationDate: null,
courses: [“”, “”, “”],
address: { // nested document
street: “123 fake st.”,
city: “Seoul”,
zip: 600100
}
})

db.students.find() db.컬렉션이름.find()
db.students.find().sort({name: 1}) // 이름 순
db.students.find().sort({name: -1}) // 이름 역순
db.students.find().sort({gpa: 1}) // GPA 점수 낮은 사람에서 높은 사람 순으로
db.students.find().limit(1) // 조회 document 개수 제한
db.students.find().sort({gpa: -1}).limit(1) // 제일 GPA 점수가 높은 한 사람 조회, limit(0)는 전체
db.students.find({name:”Spongebob”})
db.students.find({gpa:4.2})
db.students.find({fullTime: false})
db.students.find({gpa:4.0, fullTime: false})

특정 필드만 필요할 때 두번째 매개변수로 프로젝션을 전달
db.students.find({}, {name: true, gpa: true})
db.students.find({query}, {projection}) query where, projection columns in sql

db.students.updateOne(filter, update)
db.students.updateOne({name: “Spongebob”}, {$set:{fullTime: true}})
db.students.updateOne({name: “Spongebob”}, {$unset:{fullTime: “”}}) // 필드 삭제

{query}는 메서드 이름으로 하거나 또는 @Query()를 사용하고 update는 save()를 하거나 또는 일부 필드만 하려면 @Update()로 쿼리를 직접 작성한다.
@Update("{ '$inc' : { 'visits' : ?1 } }")
void findAndIncrementVisitsByLastname(String lastname, int increment);

@Query("{ 'lastname' : ?0 }")
@Update("{ '$inc' : { 'visits' : ?1 } }")
void updateAllByLastname(String lastname, int increment);

```
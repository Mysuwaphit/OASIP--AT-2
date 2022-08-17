# 🐈 OASIP||-AT-2_Ching-cheng-hanji 🐁

## ER diagram
![image](./ER_diagram.png)
## Design communications between client and server.

- Protocol => HTTP,TCP SSL,TLS
- Communication => JSON file

## API
| URL Mapping | Method | Description |
|-------------|--------|-------------|
| api/events | POST | Create event ใหม่เพิ่มลงฐานข้อมูล |
| api/events | GET | List event ทั้งหมดในฐานข้อมูล |
| api/events/{id} | GET | List event เฉพาะ id ที่ระบุ |
| api/events/{id} | DELETE | ลบ event ตาม id ที่ระบุ |
| api/events/{id} | PUT | อัพเดท event ในฐานข้อมูล |
| api/eventCategories | POST | Create eventCategory ใหม่เพิ่มลงฐานข้อมูล |
| api/eventCategories | GET | List  eventCategories ทั้งหมดในฐานข้อมูล |
| api/eventCategories/{id} | GET | List eventCategory เฉพาะ id ที่ระบุ |
| api/eventCategories/{id} | DELETE | ลบ eventCategory ตาม id ที่ระบุ |
| api/eventCategories/{id} | PUT | อัพเดท eventCategory ในฐานข้อมูล |
| api/users | POST | Create user ใหม่ลงในฐานข้อมูล |
| api/users | GET | List user ทั้งหมดในฐานข้อมูล |
| api/users/{id} | GET | List user เฉพาะ id ที่ระบุ |
| api/users/{id} | DELETE | ลบ user ตาม id ที่ระบุ |
| api/users/{id} | PUT | อัพเดท user ในฐานข้อมูลตาม id ที่ระบุ |

## Setup dev env and Software Management
- [GitHub](https://github.com/pataradee/Ching-cheng-hanji.git)
- [Trello](https://trello.com/invite/b/tKVIS3xb/06df9e0ddb9fbf59a12d30e4ebe415a9/project-management)
- Tools => 
    - Front-end : VS Code
    - Back-end : IntelliJ
    - DevOps : Docker
    - Database : MySQL workbench
- Communications => Discord, MS Team
- Port
    - Front-end : 80
    - Back-end : 8080
    - Database : 3306

## Setup dev server


## Wireframes
By [Figma](https://www.figma.com/proto/FFu7WiaBwzUe0KrRhRFeNy/Integrated?page-id=0%3A1&node-id=2%3A2&viewport=241%2C48%2C0.27&scaling=min-zoom&starting-point-node-id=2%3A2)

# 🎫 About Us
งานนี้เป็นส่วนของวิชา INT222 INFORMATION TECHNOLOGY INTEGRATED PROJECT <br/> ภาคเรียนที่ 1 ปีการศึกษา 2565 คณะเทคโนโลยีสารสนเทศ มหาวิทยาลัยเทคโนโลยีพระจอมเกล้าธนบุรี
### 🎫 Team: Ching-cheng-hanji
1. ชื่อ นาย ณัฏฐกรณ์ นามสกุล โชติภัทรจินดา StudentID : ```63130500029```
2. ชื่อ นาย ภัทรดนัย นามสกุล ดีรักษา StudentID : ```63130500090```
3. ชื่อ นางสาว สุวพิชญ์ นามสกุล  โชติสวัสดิ์ StudentID : ```63130500126```

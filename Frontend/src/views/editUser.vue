<script setup>
import { ref, onBeforeMount, computed} from "vue";
import { useRouter , useRoute } from 'vue-router';
import SuccessBox from '../components/successBox.vue'



const {params} = useRoute()
const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`
const userList = ref([])
const userListDetails = ref([]) 

const postRefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/refresh`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'Authorization': token,
          'isRefreshToken': true
        }
      })
      if(res.status === 200){
        status.value = res.status
        const response = res.json()
        response.then(jsonRes => {
         const reToken = jsonRes.jwt
         localStorage.setItem('accessToken', reToken);
         console.log(reToken)
         console.log(localStorage.getItem('accessToken'))
        })
      }else alert("Something went wrong! Please log in again.")
}
const getUserList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const user = await res.json();
    userList.value = user
    userListDetails.value = userList.value.filter((e) =>  e.id.toString() === params.userId)
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }  
  else {
    console.log("No have any users.");
  }
};
if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }
onBeforeMount(async () => {
  await getUserList();
});

const allRole = ['admin','lecturer','student']
const roleValue = ref('student')
let yourName = ref(userListDetails.username)
let yourEmail = ref(userListDetails.email)
let selectedRole = ref(roleValue.value) 
let  success = ref(false)

const updateUser = async (editUserId,validatedName,validatedEmail,selectedRole) => {
  if((validatedName===null && validatedName==='') || (validatedEmail===null && validatedEmail==='')){
    console.log('success')
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${editUserId}`,{
      method: 'PUT',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(
        {
        username: validatedName,
        email: validatedEmail,
        roles: selectedRole
        }
      )
    })
    console.log('olo')
    if (res.status === 200) {
      const add = await res.json()
      userList.value.push(add)
      clearForm();
      console.log('added successfully')
      success.value = true
    } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
    } else {
      console.log('error, cannot be added')
    }
  }else alert("Cannot input null or no value")
}

// Validate
let validatedName = ref('')
// const validateName = () => (trim(yourName.value) != null || trim(yourName.value) != ''? (validatedName.value = trim(yourName.value)): alert("Please insert your name.")) && (trim(yourName.value) != userListDetails.username.value? (validatedName.value = trim(yourName.value)) : alert("Please insert your name again! Your name must be unique."))
const validateName = () => {
  yourName.value.trim() != null && yourName.value.trim() != ''? (validatedName.value = yourName.value.trim()) : alert("Please insert your name.")
  yourName.value.length > 100? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : (yourName.value = yourName.value.trim())
  userList.value.forEach((e) => e.username != yourName.value.trim()? (yourName.value = yourName.value.trim()) : (yourName.value = '',alert("This name has been used. Please input your another name.")))
}
let validatedEmail = ref('')
const validateEmail = () => {
  const  validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  yourEmail.value.length > 50? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : (yourEmail.value = yourEmail.value.trim())
  yourEmail.value.match(validEmail)? validatedEmail.value = yourEmail.value.trim() : alert("Please insert your email again. your email is not valid.") 
  return  userList.value.forEach((e) => e.email != yourEmail.value.trim()? (yourEmail.value = yourEmail.value.trim()) : (yourEmail.value = '', alert("Please insert your email again! Your email must be unique.")))
}


const clearForm = () => {
  yourName.value = userListDetails.username
  yourEmail.value = userListDetails.email
  selectedRole = roleValue.value
  goBack()
  return console.log('clear');
}

</script>
 
<template>
    <div class="app"> 
      <h1>Edit user</h1>
        <div class="box" v-for="(userListDetail,index) in userListDetails" :key="index">
            <div class="modal-body">
              <form>
                  <div class="form-group">
                    <label class="col-form-label">Username :</label>
                    <input type="text" :placeholder="userListDetail.username" class="form-control" id="recipient-name" maxlength="100" v-model="yourName" @focusout="validateName" required> 
                  </div>
                  <div class="form-group">
                    <label class="col-form-label">Email :</label>
                    <input class="form-control" :placeholder="userListDetail.email" id="recipient-email" maxlength="50" v-model="yourEmail" @focusout="validateEmail"  required> 
                  </div>
                  <!-- Choose Role -->
                  <div>
                    <label class="col-form-label">Role :</label>
                    <select name="role" id="select" v-model="selectedRole" required>
                    <option v-for="(role,index) in allRole" :key="index" :value="role" > {{ role }} </option> 
                    </select>
                  </div>
              </form>
            </div>
              <!-- Footer -->
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
                <button type="button" class="btn btn-primary trigger-btn" id="save" href="#myModal" data-toggle="modal"
                @click="updateUser(userListDetail.id,validatedName,validatedEmail,selectedRole)">save</button>
                <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
              </div>
        </div>
    </div>
    <SuccessBox v-if="success === true"/>
</template>
 
<style lang="scss" scoped>
#bookingname::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(255, 255, 255);
}
#email{
  overflow: hidden;
  overflow-x:auto;
  border-radius: 20px;
}
#description{
  height: 450px;
  overflow: hidden;
  overflow-y:auto;
  border-radius: 20px;
}
::-webkit-scrollbar{
  padding:0px 20px;
  width: 5px;
  height:7px;
}
::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(35, 35, 35);
}
#backhome:hover {
  background-color:var(--dark);
}
#backhome{
  background-color:white;
  margin: 50px;
  margin-right: 600px; 
  width:80px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  margin-top: 90px;
  top:600px;
  left: 390px;
  position: absolute;
}
h1 {
  color: white;
  position: absolute;
  font-weight: bold;
  left:400px;
  top:50px;
  font-size:70px;

}
#delete {
  background-color: rgba(146, 44, 64, 1);
  margin: 20px;
  margin-right: 600px; 
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  margin-top: 90px;
  top:-80px;
  left: 350px;
  position: relative;
  // opacity: 1;
}

#delete:hover {
  background-color:rgb(71, 10, 22);
}

#bookingname {
  position: absolute;
  left:400px;
  top:50px;
  font-size:70px;
  font-weight: bold;
  color: white;
  width:1000px;
  height: 100px;
  overflow: hidden;
  overflow-y:auto;
}

#email {
  background-color: white;
  border-radius: 20px;
  width: 320px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:120px;
  text-align: center;
  margin: -20px;
}

#startDate {
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:180px;
  text-align: center;
  margin: -20px;
}

#endDate{
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:180px;
  text-align: center;
  margin: -20px;
}

#startTime{
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:180px;
  text-align: center;
  margin: -20px;
}

#endTime {
  background-color: white;
  border-radius: 20px;
  width: 150px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:167px;
  text-align: center;
  margin: -20px;
}

#description {
  background-color: white;
  border-radius: 20px;
  width: 370px;
  height: 120px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-35px;
  left:200px;
  text-align: center;
  margin: -20px;
}

.box {
background-color: rgba(229, 229, 229, 1);
opacity: 80%;
border-radius: 20px;
width: 1000px;
height: 600px;
margin-top: 180px;
margin-left: 400px;
}

#cancel:hover {
  background-color:rgb(71, 10, 22);
}

#save:hover {
   background-color:rgb(12, 65, 11);
}

#save {
   background-color:rgba(3, 133, 0, 1);
   margin: 30px;
   margin-right: 200px;
   position: relative;
}

#cancel {
   background-color: rgba(146, 44, 64, 1);
   margin: 30px;
   margin-right: 600px; 
}

#cancel,#save {
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  font-weight: bold;
  margin-top: 300px;
  position: absolute;
}

#select {
margin-left:30px;
margin: 5px;
}

label {
  font-weight: bold;
  font-size:20px;
}

#message-text {
  width: 500px;
  height: 100px;
  margin: 10px;
  border-radius:15px;
}

#recipient-name , #recipient-email {
  width:370px;
  margin: 10px;
  border-radius:15px;
}

input[type="datetime-local"]::-webkit-clear-button {
    display: none;
}

/* Removes the spin button */
input[type="datetime-local"]::-webkit-inner-spin-button { 
    display: none;
}

/* Always display the drop down caret */
input[type="datetime-local"]::-webkit-calendar-picker-indicator {
    color: #2c3e50;
}

/* A few custom styles for date inputs */
input[type="datetime-local"] {
    appearance: none;
    -webkit-appearance: none;
    color: rgba(196, 196, 196, 1);
    font-family: "Helvetica", arial, sans-serif;
    font-size: 18px;
    border:1px solid #ecf0f1;
    background:#ecf0f1;
    padding:5px;
    display: inline-block !important;
    visibility: visible !important;
}

input[type="datetime-local"], focus {
    color: rgba(196, 196, 196, 1);
    box-shadow: none;
    -webkit-box-shadow: none;
    -moz-box-shadow: none;
    border-radius: 10%;
}
</style>

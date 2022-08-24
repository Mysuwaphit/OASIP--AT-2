
'<script setup>
import { ref, onBeforeMount } from "vue";
import { useRouter } from 'vue-router';
import SuccessBox from '../components/successBox.vue'

const allRole = ['admin','lecturer','student']
const selectedRole = ref('student')
const yourName = ref('')
const yourEmail = ref('')

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)
const userList = ref([])
const getUserList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`)
  if (res.status === 200) {
    userList.value = await res.json(); 
  } else {
    console.log("No Category");
  }
};
onBeforeMount(async () => {
  await getUserList();

});

// const validateAll = ref(true)
let validatedName = ref('')
const validateName = () => {
  yourName.value != null && yourName.value != ''? (validatedName.value = yourName.value.trim()) : alert("Please insert your name.")
  yourName.value.length > 100? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : yourName.value = yourName.value.trim()
  userList.value.forEach((e) => e.username != yourName.value.trim()? (yourName.value = yourName.value.trim()) : (yourName.value = '',alert("This name has been used. Please input your another name.")))
  return validatedName.value = yourName.value 
}

let validatedEmail = ref('')
const validateEmail = () => {
  const  validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  yourEmail.value.length > 50? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : (yourEmail.value = yourEmail.value.trim())
  return yourEmail.value.match(validEmail)? validatedEmail.value = yourEmail.value.trim() : alert("Please insert your email again.") 
  && userList.value.forEach((e) => e.email != yourEmail.value.trim()? (yourEmail.value = yourEmail.value.trim()) : (yourEmail.value = '', alert("Please insert your email again! Your email must be unique.")))
}

const clearForm = () => {
  yourName.value = ''      
  yourEmail.value = ''
  selectedRole.value = 'student'
  console.log('Clear category');
}

const addUser = async (validatedName,validatedEmail,selectedRole) => {
  console.log('success')
   const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`,{
    method: 'POST',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify(
      {
        username: validatedName,
        email: validatedEmail,
        roles: selectedRole
        // createdOn: null,
        // updatedOn: null
      }
    )
  })
  if (res.status === 201 && res.status !== 400) {
    const add = await res.json()
    userList.value.push(add)
    console.log('added successfully')
  } else console.log('error, cannot be added')
}

</script>
 
<template>
    <div class="app">
      <h1>Add user</h1>
      <div id="boxAddCategory">
        <div class="modal-body">
            <form>
            <div class="form-group">
                <label class="col-form-label">Username :</label> {{yourName.length}}/100
                <input type="text" class="form-control" id="recipient-name" v-model="yourName" @focusout="validateName" required>
            </div>
            <div class="form-group">
                <label for="message-text" class="col-form-label">Email :</label> {{yourEmail.length}}/50
                <textarea class="form-control" id="message-text"  v-model="yourEmail" @focusout ="validateEmail"></textarea>
            </div>
            <!-- Choose Role -->
            <div>
                <label class="col-form-label">Role :</label>
                <select name="role" id="select" v-model="selectedRole" required>
                    <option v-for="(role,index) in allRole" :key="index" :value="role"> {{ role }} </option> 
                </select>
            </div>
            
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
            <button type="button" class="btn btn-primary trigger-btn" id="save"  href="#myModal" data-toggle="modal"
             @click="addUser(validatedName,validatedEmail,selectedRole)">save</button>
             <!-- :disabled="validateAll" -->
            <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
        </div>
      </div>
    </div>
    <SuccessBox/>
</template> 
<style lang="scss" scoped>
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
}

#cancel:hover{
  background-color:rgb(71, 10, 22);
}

#save:hover{
   background-color:rgb(12, 65, 11);
}

#save{
   background-color:rgba(3, 133, 0, 1);
   margin: 30px;
   margin-right: 200px;
   
}

#cancel{
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
  margin-top: 200px;
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

h1 {
  position: absolute;
  font-weight: bold;
  left:400px;
  top:50px;
  font-size:70px;

}

#boxAddCategory {
background-color: rgba(196, 196, 196, 1);
opacity: 80%;
border-radius: 20px;
width: 1000px;
height: 600px;
margin-top: 180px;
margin-left: 400px;
}

</style>
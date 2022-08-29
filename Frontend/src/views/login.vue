<script setup>
import { useRouter } from 'vue-router' 
import { ref,onBeforeMount } from "vue"; 

const appRouter = useRouter()
const goToAddUser = () => appRouter.push({ name: 'AddUser' })
const yourEmail = ref('')
const yourPassword = ref('')

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

let validatedEmail = ref('')
const validateEmail = () => {
  const  validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  yourEmail.value.length > 50? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : (yourEmail.value = yourEmail.value.trim())
  return yourEmail.value.match(validEmail)? validatedEmail.value = yourEmail.value.trim() : alert("Please insert your email again. your email is not valid.") 
  && userList.value.forEach((e) => e.email != yourEmail.value.trim()? (yourEmail.value = yourEmail.value.trim()) : (yourEmail.value = '', alert("Please insert your email again! Your email must be unique.")))
}
const validatePassword = (e) => {(e === null || e === '')?  alert('Password cannot be null'):(e.length >= 8? console.log('password>=8'): alert("Password must at least 8 and at most 14."))}

const checkUser = async (validatedEmail,yourPassword) => {
    // console.log('success')
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`,{
      method: 'POST',
      headers: {
        'content-type': 'application/json'
      },
      body: JSON.stringify(
        {
          email: validatedEmail,
          userpassword: yourPassword
        }
      )
    })
    
    if (res.status === 201 && res.status !== 400) {
      const add = await res.json()
      userList.value.push(add)
      console.log('added successfully')
      alert("Password Matched")
    }
    if(res.status === 401)alert("Password NOT Matched")
    if(res.status === 404)alert("A user with specified email DOES NOT exist.")
}
</script>
 
<template>
    <div class="loginbox modal">
        <img class="logo" src="../assets/businessman.png">
        <h1>Sign In</h1>
        <form>
            <label>Username</label>
            <input type="text" placeholder="Enter Username" :value="yourEmail" @focusout="validateEmail">
            <label>Password</label>
            <input type="password" placeholder="Enter Password" :value="yourPassword" @focusout="validatePassword(yourPassword)">
            <input type="submit" value="Sign In" @click="checkUser(validatedEmail,yourPassword)">
        </form>
        <p @click="goToAddUser">Forgot Password?</p>
    </div>
</template>
 
<style scoped>
.loginbox{
    width: 400px;
    height: 500px;
    background: #1c2833;
    color: white;
    margin: 170px auto;
    margin-left: 700px;
    border-radius: 10px;
    position: absolute;
    padding-top: 200px;
    padding: 50px;
}
.logo{
    width: 100px;
    height:100px;
    position: relative;
    top: -70px;
    left: calc(50% - 50px);
}
h1{
    text-align: center;
    margin-top: -60px;
}

label{

    font-size: 18px;
    display: block;
    margin-left: 20px;
}

input{

    width: 80%;
    margin: 0 0 20px 20px;
}

input[type="text"],input[type="password"]{
    border: none;
    background: transparent;
    border-bottom: 1px solid #fff;
    height: 40px;
    outline: none;
    color: white;
    font-size: 16px;
}

input[type="submit"]{
    border: none;
    outline: none;
    height: 40px;
    border-radius: 20px;
    background: #fb2525;
    color: white;
    font-size: 20px;
    cursor: pointer;
}

p{
    text-decoration: none;
    color: white;
    margin-left: 30%;
}

input[type="submit"]:hover{
    background: blueviolet;
}
</style>
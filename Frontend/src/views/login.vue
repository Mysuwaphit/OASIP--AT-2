<script setup>
  import { ref,onBeforeMount } from 'vue';
  import { useRouter } from 'vue-router' 
  import StatusBox from '../components/statusBox.vue';
  const appRouter = useRouter()
  const goToAddUser = () => appRouter.push({ name: 'AddUser' })
  const goToHome = () => appRouter.push({ name: 'EventListing' })
  const yourEmail = ref('')
  const yourPassword = ref('')
  let checkedUser = ref(false)
  let status = ref(0)

  const token = `Bearer ${localStorage.getItem('accessToken')}`
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
  const userList = ref([])
  const getUserList = async () => {
    const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`
    ,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token 
        }
    })
    if (res.status === 200) {
      userList.value = await res.json(); 
    } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
    } else {
      console.log("No Category");
    }
  };
  if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }
  onBeforeMount(async () => {
    if(localStorage.getItem('accessToken'))await getUserList();
  });



  let validatedEmail = ref('')
  const validateEmail = () => {
    const  validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
    // yourEmail.value.length > 50? alert("Your name is too long. It's must at least 1 character and no more than 100 character.") : (yourEmail.value = yourEmail.value.trim())
    return yourEmail.value.match(validEmail)? validatedEmail.value = yourEmail.value.trim() : alert("Please insert your email again. your email is not valid.") 
  }
  const checkUser = async (yourInputEmail,yourInputPassword) => {
    if(yourInputEmail != '' && yourInputPassword != ''){
      checkedUser.value = true
      console.log(checkedUser.value);
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/login`,{
        method: 'POST',
        headers: {
          'content-type': 'application/json'
          // 'Authorization': 'Bearer '
          // + this.token  
        },
        body: JSON.stringify(
          {
            email: yourInputEmail,
            userpassword: yourInputPassword
          }
        )
      })
      // .then(response => {
      //   status.value = response.status
      //   console.log(response.jwt) 
      //   // && response.data.accessToken
      //   // response.status === 201 && 
      //   if (response.status !== 400) {
      //     localStorage.setItem('user', response.jwt);
      //     console.log("Found the token!!!!")
      //     // return response.data;
      //   }else console.log(`response but ${response.status}`)
      // });
      if(res.status === 200){
        status.value = res.status
        const response = res.json()
        response.then(jsonRes => {
         const token = jsonRes.jwt
         const refreshToken = jsonRes.refreshToken
         localStorage.setItem('accessToken', token);
         console.log(token)
         console.log(localStorage.getItem('accessToken'))
        })
      }
    }else alert('Password cannot be null')
  }
  </script>
   
  <template>
  <!-- @edit="(i) => checkedUser = i" @success="goToHome" -->
    <StatusBox v-if="checkedUser === true" :status="status" />
    <div class="loginbox" id="demo-1">
        <img class="logo" src="../assets/businessman.png"/>
          <h1>Sign In</h1>
          <form>
              <label>Email</label>
              <input type="email" placeholder="Enter Email" v-model="yourEmail" @focusout ="validateEmail" required>
              <label>Password</label>     
              <input type="password" placeholder="Enter Password" minlength="8" maxlength="14" v-model="yourPassword" required>
              <button type="button" class="btn btn-primary trigger-btn" id="check" data-toggle="modal" data-target="#demo-2" data-dismiss="modal"  @click="checkUser(validatedEmail,yourPassword)">submit</button>
          </form>
          <p @click="goToAddUser">Didn't have account?</p>
    </div>
  </template>
   
  <style scoped>
  .close {
    position: absolute;
    top: 5px;
    right: 2px;
  }
  .loginbox{
      width: 400px;
      height: 550px;
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
      margin-top: 30px;
      width: 100px;
      height:100px;
      position: relative;
      top: -70px;
      left: calc(50% - 50px);
      z-index: 0;
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
  input[type="email"],input[type="password"]{
      border: none;
      background: transparent;
      border-bottom: 1px solid #fff;
      height: 40px;
      outline: none;
      color: white;
      font-size: 16px;
  }
  #check{
      margin-top: 20px;
      border: none;
      outline: none;
      height: 40px;
      width: 300px;
      border-radius: 20px;
      background: #fb2525;
      color: white;
      font-size: 20px;
      cursor: pointer;
  }
  p{
      margin-top: 20px;
      text-decoration: none;
      color: white;
      margin-left: 25%;
  }
  #check:hover{
      background: blueviolet;
  }
  </style>
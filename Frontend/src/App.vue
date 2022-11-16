<script setup>
import SideBar from './components/sidebar.vue' 
import Snow from './components/snow.vue'
import { useRouter } from 'vue-router'
import Login from '../src/views/login.vue';
import { ref } from 'vue'

const appRouter = useRouter()
const goToAddEvent = () => appRouter.push({ name: 'addEvent' })
const goToLogin = () => appRouter.push({ name: 'Login'})
const goToHome = () => appRouter.push({ name: 'EventListing'})
const userEmail = ref(localStorage.getItem('email'))

const logOut = () => {
  if(localStorage.getItem('accessToken')){
    localStorage.clear()
    goToHome()
    window.location.reload()
  }else
    alert("You're not logging In")
}
console.log(localStorage.getItem('role'))
console.log(userEmail.value)
// const IsLoggingIn = () => localStorage.getItem('accessToken')? console.log(`You're already log In`) : alert("Please log in first.")
</script>
 
 <template>
    <div class="app">

        <!-- navBar -->
        <div class="navBar">
          <!-- @click="IsLoggingIn" -->
          <!-- @="localStorage.getItem('role')" -->
            <SideBar />
        </div> 

        <p id="yourUsername" v-if="userEmail != null">{{ userEmail }}</p>
        <button type="button" id="login" class="btn btn-primary trigger-btn" @click="goToLogin" v-if="userEmail === null">Log In</button>
        <button type="button" id="logout" class="btn btn-primary trigger-btn" @click="logOut">Log Out</button>

        <!-- Add event -->
        <!-- <button type="button" @click="goToAddEvent" class="addEvent" id="myBtn">+ Add Event</button> -->

        <!-- Show view -->
        <Snow/>
         <router-view/>
    </div>

</template> 
 
<style lang="scss">
body {
  height: 100vh;
  background: radial-gradient(ellipse at bottom, #1b2735 0%, #090a0f 100%);
  overflow: hidden;
}

.addEvent{
  background-color: rgba(30, 38, 64, 1);
  border: none;
  color: white;
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 16px;
  position: fixed;
  bottom: 1.5%;
  right: 45%;
  z-index:1;
}
:root{
    --primary:#FD9B00;
    --grey:#64748b;
    --dark:#1e293b;
    --dark-alt:#334155;
    --light:#f1f5f9;
    --sidebar-width:200px;
}
*{
    margin:0;
    padding: 0;
    box-sizing: border-box;
    font-family:'Kanit',serif;

}
body{
    background: var(--light);
    background-image: url('./assets/lastovich.jpg');
    // radial-gradient(at 1% 17%, #c6c6bd, transparent 50%),
    // radial-gradient(at 80% 10%, #d6ccc2 , transparent 50%),
    // radial-gradient(at 90% 90%, #f5ebe0, transparent 50%),
    // radial-gradient(at 14% 91%, #d5bdaf, transparent 80%);
    background-repeat: no-repeat;
    background-size: cover;
    text-align: center;
    position: sticky;
    height: 120vh;
    overflow: auto;
}

#login:hover{
  transform: scale(1.08); 
  transition: all .2s ease-in-out;
}

#login{
  margin-left:1680px;
}

#login:active {
  background-color: rgba(146, 44, 64, 1);
  color:white;
  outline: none;
  border: none;
}

#login:focus {
  background-color: rgba(146, 44, 64, 1);
  color:white;
//   outline: none;
//   border: none;
}

#login{
  background-color: white;  
  color:black;
  // rgba(146, 44, 64, 1)
  border-radius: 40px;
  padding: 10px 15px;
  margin-right:15px;
  margin-bottom:5px;
  margin-top: 10px;
  position: absolute;
  border: none;
}

#logout:hover{
  transform: scale(1.08); 
  transition: all .2s ease-in-out;
}

#logout{
  margin-left:1770px;
}

#logout:active {
  background-color: rgba(146, 44, 64, 1);
  color:white;
  outline: none;
  border: none;
}

#logout:focus {
  background-color: rgba(146, 44, 64, 1);
  color:white;
//   outline: none;
//   border: none;
}

#logout{
  background-color: white;  
  color:black;
  // rgba(146, 44, 64, 1)
  border-radius: 40px;
  padding: 10px 15px;
  margin-right:15px;
  margin-bottom:5px;
  margin-top: 10px;
  position: absolute;
  border: none;
}

#yourUsername{
  color: white;
  margin: 15px;
  font-size:20px;
  padding: 10px 15px;
  margin-right:15px;
  margin-bottom:5px;
  margin-top: 10px;
  margin-left:1500px;
  position: absolute;
}

button{
    cursor: pointer;
    appearance: none;
    border: none;
    outline: none;
    background: none;
}

.app{
    display: flex;
    main {
        flex: 1 1 0;
        padding: 2rem;

        @media (max-width: 768px){
            padding-left: 6rem;
        }
    }
}

</style>
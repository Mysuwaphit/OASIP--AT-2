<script setup>
import SideBar from './components/Sidebar.vue' 
import { useRouter } from 'vue-router'

const appRouter = useRouter()
const goToAddEvent = () => appRouter.push({ name: 'addEvent' })

</script>
 
 <template>
    <div class="app">

        <!-- navBar -->
        <div class="navBar">
            <SideBar/>
        </div> 

        <!-- Add event -->
        <button type="button" @click="goToAddEvent" class="addEvent" id="myBtn">+ Add Event</button>

        <!-- Show view -->
         <router-view/>
    </div>
</template> 
 
<style lang="scss">
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
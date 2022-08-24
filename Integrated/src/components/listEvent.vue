 <script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router'

const appRouter = useRouter()
const goToEventDetails = (e) => appRouter.push({ name: 'EventDetail', params: { eventId: e }})
const props = defineProps({
  events: {
    type: [Array, Object],
    require: true
  }
})

const getDate = (e) =>{
  const date = ref(new Date(e[0],e[1] - 1,e[2],e[3],e[4]))
  return date.value.toLocaleDateString();
}
const getTime = (e) =>{
  const time = ref(new Date(e[0],e[1] - 1,e[2],e[3],e[4]))
  return time.value.toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
}

props.events.sort((a, b) => (a.startTime < b.startTime) ? 1 : -1);
</script>

<template>
    <div id="allEvent" class="scroll"  >
        <div id="eachEvent" v-for="(event, index) in events " :key="index">
          <div id="event" @click="goToEventDetails(event.id)">
              <span>
                    <p id="circle"></p> 
                    <p id="category">{{ event.category }}</p>
                    <p id="time"> {{ getTime(event.startTime)}}</p>
                    <p id="startdate">{{ getDate(event.startTime) }}</p>
                    <p id="duration" >{{ event.duration + " Minutes"}}</p>
                    <p id="bookingname">{{ event.bookingName }}</p>
              </span>            
          </div>  
        </div>
    </div>
</template>
 
<style lang="scss" scoped>
.scroll{
  height: 460px;
  width: 1050px;
  overflow: hidden;
  overflow-y:auto;
  border-radius: 20px;
}
::-webkit-scrollbar{
  padding:0px 20px;
  width: 7px;
}
::-webkit-scrollbar-thumb{
  border-radius: 20px;
  background: rgb(241, 241, 241, 1);
}
#startdate{
  position: relative;
  left:430px;
  bottom:110px;
}
#duration{
  position: relative;
  left: 600px;
  bottom:150px;
}
#category{
  position: relative;
  left: 70px;
  bottom:30px;
}
#time{
  position: relative;
  left:270px;
  background-color: rgba(196, 196, 196, 1);
  border-radius: 20px;
  width: 90px;
  bottom: 70px;
  text-align: center;
}
#bookingname {
  left: 780px;
  bottom:190px;
  align-items: center;  
  position: relative;
  width: 200px;
  height:20px;
  overflow: hidden;
  overflow-y:auto;
}
#circle {
  width: 15px;
  height: 15px;
  background: red;
  border-radius: 50%;
  margin: 10px;
  margin-top: 15px ;
  margin-left:18px ;
  position:relative;
}
.material-symbols-outlined {
  position:absolute;
  left:1060px;
  margin-top: -1.75em;
  cursor: pointer;
}
#event {
  margin: 15px;
  padding: 2px;
  background-color: rgba(241, 241, 241, 1);
  border-radius: 20px;
  width: 1000px;
  height: 50px;
  position: relative;
  display: block;
  align-items: center;
}
#event:hover {
  transform: scale(1.02); 
  transition: 0.3s;
}

#router:link {
  text-decoration: none;
  color:black;
}
#router:visited {
  text-decoration: none;
  color:black;
}
#router:hover {
  text-decoration: none;
  color:black;
  
}
#router:active {
  text-decoration: none;
  color:black;
}

</style>
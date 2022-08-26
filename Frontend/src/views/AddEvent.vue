<script setup>
import { ref, onBeforeMount, computed} from "vue";
import { useRouter } from 'vue-router';
import SuccessBox from '../components/successBox.vue'

const now = new Date().toISOString().substring(0,16)
const selectCategory = ref([])
const bookingEmail = ref('')
const bookingName = ref(null)
const yourDateTime = ref(now)
const description = ref('')
// let success = ref(false)

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

const eventList = ref([])
const eventListByCategory = ref([])
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`)
  if (res.status === 200) {
    const event = await res.json();
    eventList.value = event
  } else {
    console.log("No Scheduled Events");
  }
};

const categoryList = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`)
  if (res.status === 200) {
    categoryList.value = await res.json(); 
  } else {
    console.log("No Category");
  }
};

onBeforeMount(async () => {
  await getCategory();
  await getEventList();
});

const addEvent = async (validatedName,validatedEmail,selectCategory,yourISODateTime,description,getDuration) => {
  console.log('success')
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
    method: 'POST',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify(
      {
        bookingEmail: validatedEmail,
        bookingName: validatedName,
        category: selectCategory.eventCategoryName,
        startTime: yourISODateTime,
        eventNotes: description,
        eventCategory: selectCategory,
        duration : getDuration
      }
    )
  })
  console.log('olo'+ selectCategory.eventCategoryName)
  if (res.status === 201 && res.status !== 400) {
    const add = await res.json()
    eventList.value.push(add)
    // success.value = true
    clearForm();
    console.log('added successfully')
  } else {
    console.log('error, cannot be added')
  }
}

const validateTime = () => {
  const currentTime = new Date().toISOString()
  const selectTime  = new Date(yourDateTime.value).toISOString()
  console.log(`currentTime : ${currentTime}`);
  console.log(`selectTime : ${selectTime}`);
  return (selectTime > currentTime) ? yourDateTime.value = selectTime : alert('Your input Date & Time must be present or future')
}

const getDuration = ref(0)
const checkCategory = () => { 
  getDuration.value = selectCategory.value.duration
  return eventListByCategory.value = eventList.value.filter((event) => event.eventCategory.id === selectCategory.value.id)
}  

let validatedEmail = ref('')
const validateEmail = (input) => {
  const  validEmail = /^[a-zA-Z0-9.!#$%&*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/;
  return input.match(validEmail)? validatedEmail.value = input : alert("Please insert your email again.")
}

// Handle Date & Time
const getDate = (e) =>{
  const date = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
  return date.value.toLocaleDateString();
}
const getTime = (e) =>{
  const time = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
  return time.value.toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
}

const getEndTime = (e) =>{
   const endTime = ref(new Date(e.startTime[0],e.startTime[1] - 1,e.startTime[2],e.startTime[3],e.startTime[4]))
  //  console.clear();
   return new Date(endTime.value.getTime() + getDuration.value * 60000).toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
}

const validateDateTime = ref(true)
const overlapping = computed(() => {
  validateTime();
  const date = new Date(yourDateTime.value).toLocaleDateString();
  const time = new Date(yourDateTime.value).toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
  const endChooseTime = new Date(new Date(yourDateTime.value).getTime() + getDuration.value * 60000).toLocaleTimeString('en-US', {hour: '2-digit', minute: '2-digit'})
  const validate = ref(false)
  if(eventListByCategory.value.length != 0){
    eventListByCategory.value.forEach(e => {
    const endDate = ref(getDate(e))
    const validateDate  = ref((endDate.value == date)? true : false)

    const startTime = ref(getTime(e))
    const endTime = ref(getEndTime(e))
    const timeRange =  (startTime.value < endChooseTime && endTime.value > time)? true : false

    validate.value = validateDate.value && timeRange
    return validate.value === false? (validateDateTime.value = false) : (validateDateTime.value = true,(alert('Please,Check your inputed Date&Time')))
  })}
  else validateDateTime.value = false
})

let validatedName = ref('')
const validateName = () => bookingName.value != null && bookingName.value != ''? (validatedName.value = bookingName.value) : alert("Please insert your name.")

const yourISODateTime = computed(() => {
  return new Date(yourDateTime.value).toISOString()
})

const clearForm = () => {
  yourDateTime.value = now
  selectCategory.value = ''
  bookingEmail.value = ''
  bookingName.value = ''
  description.value = ''
  return console.log('clear');
}

</script>
 
<template>
    <div class="app">
      <h1>Add Event</h1>
      <div id="boxAddEvent">
        <div class="modal-body">
            <form>
            <div class="form-group">
                <label class="col-form-label">Your name :</label>
                <input type="text" placeholder="E.g: Wang Jackson" class="form-control" id="recipient-name" maxlength="100" v-model="bookingName" @focusout="validateName()" required> 
            </div>
            <div class="form-group">
                <label class="col-form-label">Email :</label>
                <input type="email" class="form-control" placeholder="js@gmail.com" id="recipient-email" maxlength="200" v-model="bookingEmail" @focusout="validateEmail(bookingEmail)" required> 
            </div>

            <!-- Choose Category -->
            <div>
            <label class="col-form-label">Category :</label>
              <select name="category" id="select" v-model="selectCategory" @vnode-updated="checkCategory" required>
                  <option v-for="(category,index) in categoryList" :key="index" :value="category" > 
                  {{ category.eventCategoryName }}</option> 
              </select>
            </div>

            <!-- Handle datetime -->
            <div class="form-group">
              <label class="col-form-label">Date & Time :</label>
              <div>
                <span>     
                  <input type="datetime-local" v-model="yourDateTime" :min="now" @focusout="overlapping" required/> 
                </span> 
              </div> 
            </div>

            <!-- Description -->
            <div class="form-group">
                <label for="message-text" class="col-form-label">Description :</label>
                <textarea class="form-control" id="message-text" maxlength="500" v-model="description" placeholder="Typing details ..."></textarea> 
            </div>
            </form>
        </div>

        <!-- Footer -->
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
            <button type="button" class="btn btn-primary trigger-btn" id="save" href="#myModal" data-toggle="modal" :disabled="validateDateTime"
            @click="addEvent(validatedName,validatedEmail,selectCategory,yourISODateTime,description,getDuration)">save</button>
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
  top:720px;
  left: 390px;
  position: absolute;
}
h1 {
  color: white;
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
   position: relative;
}

#cancel,#save {
  width:160px;
  height:50px;
  border-radius:30px;
  font-size: 25px;
  font-weight: bold;
  margin-top: 150px;
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

#boxAddEvent {
background-color: rgba(196, 196, 196, 1);
opacity: 80%;
border-radius: 20px;
width: 1000px;
height: 700px;
margin-top: 180px;
margin-left: 400px;
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

.trigger-btn {
	display: inline-block;
	margin: 100px auto;
}

.modal-confirm .btn:hover, .modal-confirm .btn:focus {
	background: #6fb32b;
	outline: none;
}
</style>
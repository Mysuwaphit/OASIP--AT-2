<script setup>
import { ref, onBeforeMount, computed} from "vue";
import { useRouter } from 'vue-router';
import SuccessBox from '../components/successBox.vue'
import Alert from '../components/alertfilebox.vue'

const now = new Date().toISOString().substring(0,16)
const selectCategory = ref([])
const bookingEmail = ref(localStorage.getItem('email'))
const bookingName = ref(null)
const yourDateTime = ref(now)
const description = ref('')
const alertFile = ref(false)


localStorage.getItem('role')? localStorage.getItem('role'):localStorage.setItem('role','guest')
const role = localStorage.getItem('role')
// let success = ref(false)

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`


const postRefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/refresh`,{
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
         window.location.reload();
        })
      }else  alert("Something went wrong! Please log in again.")
}

const eventList = ref([])
const eventListByCategory = ref([])
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const event = await res.json();
    eventList.value = event
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } else {
    console.log("No Scheduled Events");
  }
};

const categoryList = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    categoryList.value = await res.json(); 
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } 
  else {
    console.log("No Category");
  }
};
if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  }
onBeforeMount(async () => {
  await getCategory();
  if(role != "guest")await getEventList();
});

//Upload File
const isUserUploadFile = ref(false)
let file = new Array() 
const formData = new FormData();
const uploadFile = (event) =>{
  isUserUploadFile.value = true
  file[0] = event.target.files[0]
  console.log(`isUserUploadFile: ${isUserUploadFile.value}`)
  console.log(file[0].name)
  console.log(`file size : ${file[0].size}(your) vs 10485760`) 
  if(file[0].size <= 10485760) {
    // alertFile.value = false;
    console.log(`Your file size is allow`)}
  else{
    // alertFile.value = true;
    alert('Your file is too large!!! It could be less than 10 MB.')
    file.splice(0,1)
  }
  console.log("alertfile : " + alertFile.value)
    formData.append('file', file[0]);
    console.log(`formData : ${formData.get('file').name}`)
  return formData,file
}

// Remove File
const removeFile = () => {
  file.splice(0,1)
  // this.$refs.file.reset();
  formData.delete('file')
  console.log(`removed file`)
}

const eventStatus = ref(false)
const fileStatus = ref(false)
const addEvent = async (validatedName,validatedEmail,selectCategory,yourISODateTime,description,getDuration,formData) => {
  let eventId 
  // Upload Event by login checking
  if(localStorage.getItem('accessToken')){
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
        method: 'POST',
        headers: {
          'content-type': 'application/json',
          'Authorization': token
        },
        body: JSON.stringify(
          {
            bookingEmail: validatedEmail,
            bookingName: validatedName,
            category: selectCategory.eventCategoryName,
            startTime: yourISODateTime,
            eventNotes: description,
            eventCategory: selectCategory,
            duration : getDuration,
          }
        )
      }).then(res => res.json())
    .then(data => {
      // isEvent.value = 'event'
      // console.log(`data : ${data.value}`)
      eventStatus.value = true
      if(data.status === 201 && data.status !== 400) {
        eventList.value.push(data) 
        clearForm()
      }
      eventId = data.id
      console.log(`eventStatus : ${eventStatus.value}`)
      })
    .catch(err => console.log(err))
  }else{
      const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
        method: 'POST',
        headers: {
          'content-type': 'application/json',
        },
        body: JSON.stringify(
          {
            bookingEmail: validatedEmail,
            bookingName: validatedName,
            category: selectCategory.eventCategoryName,
            startTime: yourISODateTime,
            eventNotes: description,
            eventCategory: selectCategory,
            duration : getDuration,
          }
        )
    }).then(res => res.json())
    .then(data =>  {
      // isEvent.value = 'event'
      eventStatus.value = true
      if(data.status === 201 && data.status !== 400) {
        eventList.value.push(data) 
        clearForm()
      }
      return eventId = data.id
      })
    .catch(err => console.log(err))
  }
  
  // Upload File 
  if(isUserUploadFile.value === true){
    console.log('Upload File process')
    console.log(`In Fetch : ${formData.get('file').name}`)
    if(localStorage.getItem('accessToken')){
    const resFile = await fetch(`${import.meta.env.VITE_BASE_URL}/files/${eventId}`,{
      method: 'POST',
      headers: {
        // 'Content-Type': 'multipart/form-data',
        'Authorization': token
      },
      body : formData
    }).then(res => res.json())
    .then(data => {
      console.log(`successfully added file ${data}`)
      if(data.status === 201) fileStatus.value = true
    })
    .catch(err => console.log(err))
    }else{
      const resFile = await fetch(`${import.meta.env.VITE_BASE_URL}/files/${eventId}`,{
      method: 'POST',
      // headers: {
      //   'Content-Type': 'multipart/form-data'
      // },
      body: formData
      
    }).then(res => res.json())
    .then(data => {
      console.log(data)
      if(data.status === 201) fileStatus.value = true
    })
    .catch(err => console.log(err))
    }
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
const overlapping = ref(() => {
  console.log(`it is overlap method`)
  if(role != 'guest'){
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
  }else {
    validateDateTime.value = false
    console.log(`Overlap!!`)
  }
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
                <label class="col-form-label">Your name * : </label>
                <input type="text" placeholder="E.g: Wang Jackson" class="form-control" id="recipient-name" maxlength="100" v-model="bookingName" @focusout="validateName()" required> 
            </div>
            <div class="form-group">
                <label class="col-form-label">Email * : </label>
                <input v-if="role === 'admin' || role === 'guest'" type="email" class="form-control" placeholder="js@gmail.com" id="recipient-email" maxlength="200" v-model="bookingEmail" @focusout="validateEmail(bookingEmail)" required> 
                <p type="email" class="form-control" id="recipient-email" v-if="role === 'student'">{{ bookingEmail }}</p>
            </div>

            <!-- Choose Category -->
            <div>
            <label class="col-form-label">Category * : </label>
              <select name="category" id="select" v-model="selectCategory" @vnode-updated="checkCategory" required>
                  <option v-for="(category,index) in categoryList" :key="index" :value="category" > 
                  {{ category.eventCategoryName }}</option> 
              </select>
            </div>

            <!-- Handle datetime -->
            <div class="form-group">
              <label class="col-form-label">Date & Time * : </label>
              <div>
                <span>     
                  <input type="datetime-local" v-model="yourDateTime" :min="now" @change="overlapping"  required/> 
                  <!--  -->
                </span> 
              </div> 
            </div>

            <!-- Description -->
            <div class="form-group">
                <label for="message-text" class="col-form-label">Description :</label>
                <textarea class="form-control" id="message-text" maxlength="500" v-model="description" placeholder="Typing details ..."></textarea> 
            </div>

            <!-- File -->
            <div class="form-group">
                <label for="message-text" class="col-form-label">Your file :</label>
                <input type="file" ref="file" class="form-control" id="recipient-email" @change="uploadFile"/>
                <button @click="removeFile" class="close-icon" aria-label="Remove"><span class="material-symbols-outlined" @click="isUserUploadFile = false">delete_forever</span></button>
            </div>
            </form>
        </div>

        <!-- Footer -->
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
            <button type="button" class="btn btn-primary trigger-btn" id="save" href="#myModal" data-toggle="modal" :disabled="validateDateTime"
            @click="addEvent(validatedName,validatedEmail,selectCategory,yourISODateTime,description,getDuration,isUserUploadFile === true? formData: formData.append('file', 'no file'))">save</button>
            <!--  -->
            <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
        </div>
      </div>
    </div>
     <Alert v-if="alertFile === true" /> 
    <SuccessBox/>
</template> 
<style lang="scss" scoped>
.close-icon{
  position: absolute;
  padding: 0.5%;
  margin-left: 400px; 
  margin-top: -50px;
  background: #ff5858;
  border-radius:10px;
}
.close-icon:hover{
  background-color:rgb(71, 10, 22);
  transition: all .1s ease-in-out;
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
  margin-top: 290px;
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
height: 900px;
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
<script setup>
import { ref, onBeforeMount } from "vue";
import { useRouter , useRoute } from 'vue-router';
import SuccessBox from '../components/successBox.vue'

const noteValue = ref('')
const durationValue = ref()
const description = ref(noteValue)
const duration = ref(durationValue)
let  success = ref(false)

const {params} = useRoute()
const appRouter = useRouter()
const goBack = () => appRouter.go(-1)

const categoryListDetails = ref([])
const categoryList = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`)
  if (res.status === 200) {
    const List = await res.json(); 
    categoryList.value = List
    categoryListDetails.value= List
  } else {
    console.log("No Category");
  }
};

onBeforeMount(async () => {
  await getCategory();
  matchCat();
});

  // Match Event with param on URL
const matchCat = () => categoryListDetails.value = categoryListDetails.value.filter((cat) => cat.id === +(params.catId))

const updateEvent = async (categoryListDetailId,duration,description) => {
  console.log('success')
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories/${categoryListDetailId}`,{
    method: 'PUT',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify(
      {
      duration: duration,
      eventCategoryDescription: description
      }
    )
  })
  console.log('olo'+ duration)
  if (res.status === 200) {
    const add = await res.json()
    categoryList.value.push(add)
    clearForm();
    console.log('added successfully')
    success.value = true
  } else {
    console.log('error, cannot be added')
  }
}

const validateAll = ref(true)
const validDuration = () => duration.value > 480 || duration.value === 0? (validateAll.value = true,alert("The duration must be at least 1 character and less than or equal to 480.")): (validateAll.value = false,(duration.value = duration.value))
const validateDescription = () => description.value.length > 500? (validateAll.value = true,alert("Your category name is too long. It's must be no more than 500 character.")) : (validateAll.value = false,(description.value = description.value))

const clearForm = () => {
  duration.value = durationValue.value
  description.value = noteValue.value
  return console.log('clear');
}

</script>
 
<template>
  
    <div class="app"> 
        <div class="box" v-for="(categoryListDetail,index) in categoryListDetails " :key="index">
            <h1 id="bookingname">{{ categoryListDetail.eventCategoryName }}</h1>
              <div id="detail">
                <form @vnode-before-mount="durationValue=categoryListDetail.duration">
                 <div class="form-group">
                    <label class="col-form-label">Duration (1-480 Minutes) :</label>
                    <input type="number" class="form-control" id="recipient-email" min="1" max="480" v-model="duration" @focusout="validDuration" required>
                </div>

                <!-- Description -->
                <div class="form-group">
                    <label for="message-text" class="col-form-label">Description :</label> {{description.length}}/500
                    <textarea class="form-control" id="message-text"  type="text" v-model="description" 
                    @vnode-before-mount="noteValue = categoryListDetail.eventCategoryDescription" @focusout="validateDescription">
                    {{categoryListDetail.eventCategoryDescription}}</textarea> 
                </div>
                </form>
              </div>
              <!-- Footer -->
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
                <button type="button" class="btn btn-primary trigger-btn" id="save" href="#myModal" data-toggle="modal" 
                :disabled="validateAll" @click="updateEvent(categoryListDetail.id,duration,description)">save</button>
                <button type="button" class="material-symbols-outlined" @click="goBack" id="backhome">arrow_back</button>
              </div>

        </div>
    </div>
    <SuccessBox href="#myModal" data-toggle="modal" v-if="success === true"/>
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
  top:700px;
  left: 390px;
  position: absolute;
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

#categoryname {
  background-color: white;
  border-radius: 20px;
  width: 300px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:250px;
  text-align: center;
  margin: -20px;
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

#duration {
  background-color: white;
  border-radius: 20px;
  width: 160px;
  padding: 5px;
  position: relative;
  font-size: 20px;
  top:-37px;
  left:160px;
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
height: 700px;
margin-top: 180px;
margin-left: 400px;
padding: 50px;
font-size: 30px;
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

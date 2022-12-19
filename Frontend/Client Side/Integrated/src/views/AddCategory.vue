<script setup>
import { ref, onBeforeMount } from "vue";
import { useRouter } from 'vue-router';
import SuccessBox from '../components/successBox.vue'

const duration = ref(1)
const categoryName = ref('')
const description = ref('')

const appRouter = useRouter()
const goBack = () => appRouter.go(-1)
const categoryList = ref([])
const getCategory = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategory`)
  if (res.status === 200) {
    categoryList.value = await res.json(); 
    // console.log(categoryList.value); 
  } else {
    console.log("No Category");
  }
};
onBeforeMount(async () => {
  await getCategory();

});


const addCategory = async (bookingName,duration,description) => {
  console.log('success')
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategory`,{
    method: 'POST',
    headers: {
      'content-type': 'application/json'
    },
    body: JSON.stringify({
    eventCategoryName: bookingName,
    duration: duration,
    eventCategoryDescription: description})
  })
  console.log(bookingName)
  if (res.status === 201) {
    const add = await res.json()
    categoryList.value.push(add)
    console.log('added successfully')
  } else console.log('error, cannot be added')
}
  
const clearForm = () => {
  categoryName.value = ''      
  duration.value = 1
  description.value = ''
  console.log('Clear category');
}

</script>
 
<template>
    <div class="app">
      <h1>Add Category</h1>
      <div id="boxAddCategory">
        <div class="modal-body">
            <form>
            <div class="form-group">
                <label class="col-form-label">Category name :</label>
                <input type="text" class="form-control" id="recipient-name" maxlength="100" v-model="categoryName" required>
            </div>
            <div class="form-group">
                <label class="col-form-label">Duration (1-480 Minutes) :</label>
                <input type="number" class="form-control" id="recipient-email" min="1" max="480" v-model="duration" required>
            </div>
            <div class="form-group">
                <label for="message-text" class="col-form-label">Description :</label>
                <textarea class="form-control" id="message-text"  maxlength="500" v-model="description"></textarea>
            </div>
            </form>
        </div>
        <div class="modal-footer">
            <button type="button" class="btn btn-secondary" id="cancel" data-dismiss="modal" @click="clearForm()">cancel</button>
            <button type="button" class="btn btn-primary trigger-btn" id="save"  href="#myModal" data-toggle="modal" @click="addCategory(categoryName,duration,description)">save</button>
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
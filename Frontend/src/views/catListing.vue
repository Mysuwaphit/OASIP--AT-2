<script setup>
import { ref, onBeforeMount } from "vue";
import ListCategory from '../components/listCategory.vue'

const token = `Bearer ${localStorage.getItem('accessToken')}`
const eventList = ref([])

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
        // status.value = res.status
        const response = res.json()
        response.then(jsonRes => {
         const reToken = jsonRes.jwt
         localStorage.setItem('accessToken', reToken);
         console.log(reToken)
         console.log(localStorage.getItem('accessToken'))
        })
      }else alert("Something went wrong! Please log in again.")
}
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    eventList.value = await res.json();
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } 
  else {
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
onBeforeMount(async () => {
  await getCategory();
  await getEventList();
});

const selectedCat = ref({})
const checkCat = (e) =>  eventList.forEach(event => e.id === event.eventCategory.id)? selectedCat.value = e : []


</script>
 
<template>
    <div class="app">
      <div id="allCategory">
        <h1 id="categoryTitle">Category</h1>
        <ListCategory id="listCate" :categories="categoryList" @selectCat="selectedCat"/>
      </div> 
    </div>
</template>
 
<style lang="scss" scoped>
#categoryTitle{
  position: relative;
  font-weight: bold;
  text-align: right;
  margin: 10px;
  font-size:70px;
  color: white;
}
#allCategory{
margin-top: 30px;
margin-left: 350px;
font-size: 25px;
}
#listCate{
  padding-left: 50px;
  width: 1100px;
  height: 800px;
}
</style>
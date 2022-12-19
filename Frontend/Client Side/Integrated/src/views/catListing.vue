<script setup>
import { ref, onBeforeMount } from "vue";
import ListCategory from '../components/listCategory.vue'
import ListEvent from "../components/listEvent.vue";

const eventList = ref([])
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/event`)
  if (res.status === 200) {
    eventList.value = await res.json();
    // console.log(eventList.value);
  } else {
    console.log("No Scheduled Events");
  }
};

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
  await getEventList();
});

const isMatch = ref('') 
const selectedCat = ref({})
const checkCat = (e) =>  eventList.forEach(event => e.id === event.eventCategory.id)? selectedCat.value = e : []
// console.log(selectedCat);


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
  position: absolute;
  font-weight: bold;
  left:1100px;
  top:50px;
  font-size:70px;
  color: white;
}
#allCategory{
margin-top: 130px;
margin-left: 150px;
font-size: 25px;
}
#listCate{
  padding-left: 50px;
}
</style>
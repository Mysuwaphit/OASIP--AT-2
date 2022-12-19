<script setup>
    import { ref, computed } from 'vue'
    const yourDate = ref('2022-04-26')
    const yourDateTime = ref('2022-04-26T15:30')
    const yourISODate = computed(() => {
    return new Date(yourDate.value).toISOString()
    })
    const yourISODateTime = computed(() => {
    return new Date(yourDateTime.value).toISOString()
    })
    const onedayPlus = computed(() => {
    const tmp = new Date(yourDate.value).getTime() + 24 * 60 * 60 * 1000  
    return new Date(tmp).toLocaleDateString('th-TH')
    })

const currentDate = new Date();
const currentDayOfMonth = currentDate.getDate();
const currentMonth = currentDate.getMonth(); // Be careful! January is 0, not 1
const currentYear = currentDate.getFullYear();



const dateString = currentDayOfMonth + "-" + (currentMonth + 1) + "-" + currentYear;
// "27-11-2020"
</script>
<template>  
    <div>    
     <div>   
        your date: <input type="date" v-model="yourDate" />{{ yourDate }}
      <p>ISO Date: {{ yourISODate }}</p>      
      <p>(+)1 Day: {{ onedayPlus }}</p>    
     </div>    
     <div>      
         your date time: <input type="datetime-local" v-model="yourDateTime" />{{ yourDateTime }}
       <p>ISO DateTime: {{ yourISODateTime }}</p>    
     </div>  


    </div>
</template>
<style></style>
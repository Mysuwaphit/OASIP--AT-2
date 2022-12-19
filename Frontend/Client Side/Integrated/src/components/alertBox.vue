<script setup>
import { ref } from 'vue'

const toggle = ref(false)
// console.log(toggle.value);

</script>
 
<template>
    <div class="container" v-if="toggle === false">
        <div type="button" class="close-bold" @click="toggle = true">✖</div>
        <h1 class='text t1'>C:\Users\> <span class='textClass'>Sorry</span></h1>
        <h1 class='text t2'> C:\Users\> No Scheduled Events.</h1>
    </div>

</template>
 
<style scoped >
.container {
  background-color: rgba(30, 38, 64, 1);
  position: absolute;
  display: flex;
  flex-direction: column;
  justify-content: center;
  min-height: 50vh;
  margin-top: 20%;
  left: 50%;
  transform: translate(-50%, -50%);
  color: white;
  margin: 100;
  font-family: "Roboto Mono", monospace;
  border-radius: 15% / 50%;
}

.text {
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
}

.textClass {
  color: rgba(146, 44, 64, 1);
}

.t1 {
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  animation: type 2s steps(20, end), curser1 0.8s 4;
  border-right: 8px solid transparent;
  animation-delay: 0;
  transition: 0.2s;
  padding-left: 2%;
  padding: 50px;
  margin-left: 100px ;
  margin-top: 80px;
}

.t2 {
  margin: 0;
  white-space: nowrap;
  overflow: hidden;
  animation: type 2s steps(19, end), curser 0.8s infinite;
  border-right: 8px solid white;
  animation-fill-mode: both;
  animation-delay: 3s;
  padding-left: 2%;
  padding: 50px;
  margin-left: 100px ;
}

.t1:active {
  border-right: 8px solid transparent;
}

@keyframes type {
  0% {
    width: 0;
  }
  100% {
    width: 100%;
  }
}

@keyframes curser {
  0% {
    border-color: transparent;
  }
  50% {
    border-color: white;
  }
  100% {
    border-color: transparent;
  }
}

@keyframes curser1 {
  0% {
    border-color: transparent;
  }
  50% {
    border-color: white;
  }
  100% {
    border-color: transparent;
  }
}

[class*='close-'] {
  color: rgb(255, 255, 255);
  font: 14px/100% arial, sans-serif;
  position: absolute;
  padding-top: 8%;
  right: 8%;
  text-decoration: none;
  text-shadow: 0 1px 0 #fff;
  top: 5px;
}

#disable{
  display: none;
}

#enable{
  display: inline-block;
}


</style>
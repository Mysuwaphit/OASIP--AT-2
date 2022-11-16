<script setup>
import { onMounted, onUnmounted } from 'vue'
const emit = defineEmits(['files-dropped'])

function onDrop(e) {
    emit('files-dropped', [...e.dataTransfer.files])
}

</script>
 
<template>
     <!-- add `data-active` and the event listeners -->
    <div @drop.prevent="onDrop">
        <!-- share state with the scoped slot -->
        <slot></slot>
    </div>
</template>
 
<style>

</style>
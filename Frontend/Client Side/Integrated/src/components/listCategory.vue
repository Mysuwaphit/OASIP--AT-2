<script setup>
import { useRouter } from 'vue-router'

const appRouter = useRouter()
const goToCatDetails = (e) => appRouter.push({ name: 'CatDetail', params: { catId: e }})

defineEmits(['selectCat'])
defineProps({
  categories: {
    type: [Array, Object],
    require: true
  }
})
</script>
 
<template>
    <div id="allCategory" v-for="(category, index) in categories" :key="index">
        <span id="eachCategory" @click="goToCatDetails(category.id)"  @onmouseup ="$emit('selectCat',category)">
                <span>
                    <p id="categoryName">{{ category.eventCategoryName}}</p>  
                    <p id="categoryDuration">{{"Duration : " + category.duration + " Minutes"}}</p>
                </span>
                <!-- <router-link id="router" :to="{ name: 'CatDetail', params: { catId: category.id }}"  @onmouseup ="$emit('selectCat',category)">
                <p>juu</p>
            </router-link> -->
        </span>
    </div>
</template>
 
<style lang="scss" scoped>
#categoryName{
  font-weight:bold;
  margin-bottom: 20px;
}

#allCategory {
  float:left;
}

#eachCategory {
  z-index: 0;
  margin:10px;
  padding: 10px;
  background-color: rgba(229, 229, 229, 1);
  border-radius: 20px;
  width: 500px;
  position: relative;
  float:left;
  transition: all .2s ease-in-out;
}

#eachCategory:hover {
  transform: scale(1.05); 
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

#categoryDuration {
  text-decoration: none;
}
</style>
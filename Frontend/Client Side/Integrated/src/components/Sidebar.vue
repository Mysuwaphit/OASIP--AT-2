<script setup>
import { ref } from "vue";
import { useRouter } from 'vue-router'

const appRouter = useRouter()
const goHome = () => appRouter.push({ name: 'EventListing'})
const toggle = ref(false);
const ToggleMenu = () => {
  toggle.value = !toggle.value;
};
</script>

<template>
  <aside :class="`${toggle && 'toggle'}`">
    <div class="logo">
      <img src="../assets/logo-sit.png" alt="sit" @click="goHome"/>
    </div>

    <div class="menu-toggle-wrap">
      <button class="menu-toggle">
        <span class="material-symbols-outlined" @click="ToggleMenu">
          keyboard_double_arrow_right
        </span>
      </button>
    </div>

    <h3>Menu</h3>
    <div class="menu">
      <router-link class="button" to="/">
        <span class="material-symbols-outlined"> event_available </span>
        <span class="text">All Event</span>
      </router-link>

      <router-link class="button" to="/allCategory">
        <span class="material-symbols-outlined"> category </span>
        <span class="text">All Category</span>
      </router-link>

      <router-link class="button" to="addevent">
        <span class="material-symbols-outlined"> calendar_add_on </span>
        <span class="text">Add Event</span>
      </router-link>

      <router-link class="button" to="addcategory">
        <span class="material-symbols-outlined"> add </span>
        <span class="text">Add Category</span>
      </router-link>
    </div>
  </aside>
</template>

<style lang="scss" scoped>

aside {
  z-index: 1;
  position: fixed;
  display: flex;
  flex-direction: column;
  width: calc(2rem + 32px);
  background-color: var(--dark);
  color: var(--light);
  overflow: hidden;
  min-height: 100vh;
  padding: 1rem;
  transition: 0.2s ease-out;

  .logo {
    margin-bottom: 1rem;

    img {
      width: 2.5rem;
    }
  }

  .menu-toggle-wrap {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 1rem;
    position: relative;
    top: 0;
    transition: 0.2s ease-out;

    .menu-toggle {
      transition: 0.2s ease-out;

      .material-symbols-outlined {
        font-size: 2rem;
        color: var(--light);
      }

      &:hover {
        .material-symbols-outlined {
          color: var(--primary);
          transition: 0.3s ease-out;
          transform: translateX(0.5rem);
        }
      }
    }
  }
  h3, .button .text{
      opacity: 0;
      transition: 0.3s ease-out;
  } 
  h3 {
      color:gray;
      font-size: 0.875rem;
      text-transform: uppercase;
  }

  .menu{
      margin:0 -1rem;

      .button{
          display: flex;
          align-items: center;
          text-decoration: none;

          padding: 3rem 1rem;
          transition: 0.3s ease-out;

          .material-symbols-outlined{
              font-size: 2.3rem;
              color: var(--light);
              transition: 0.2s ease-out;
          } 

          .text{
              color: var(--light);
          }
          &:hover{
              background-color: var(--dark-alt);
              .material-symbols-outlined , .text{
                color:var(--primary)
              }
          }
      }
  }

  &.toggle {
    width: var(--sidebar-width);
    .menu-toggle-wrap {
      top: -3rem;
      .menu-toggle {
        transform: rotate(-180deg);
      }
    }

      h3, .button .text{
      opacity: 1;
      transition: 0.3s ease-out;
  } 
  }

  @media (max-width: 768px) {
    position: fixed;
    z-index: 99;
  }
}
</style>

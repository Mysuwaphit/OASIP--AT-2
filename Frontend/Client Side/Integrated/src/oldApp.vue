<script setup>

</script>

 
<template>
    <div class="Body">
        <link href='https://unpkg.com/boxicons@2.1.2/css/boxicons.min.css' rel='stylesheet'>

        <div class="sidebar">
            <div class="logo_content">
                <div class="logo">
                    <i class='bx bxs-cat' ></i>
                    <div class="logo_name">Event</div>
                </div>
                <i class='bx bx-menu' id="btn-menu"></i>
            </div>
            <ul class="nav-list">
                <li>
                    <a href="#">
                        <i class='bx bxs-calendar' ></i>
                        <span class="link">All Event</span>
                    </a>
                    <!-- <span class="tooltip>All Date</span>"-->
                </li>
                <li>
                    <a href="#">
                        <i class='bx bxs-timer' id="btn-alldate"></i>
                        <span class="link">All Date</span>
                    </a>
                    <!-- <span class="tooltip>All Date</span>"-->
                </li>
                <li>
                    <a href="#">
                        <i class='bx bxs-timer' id="btn-alldate"></i>
                        <span class="link">All Date</span>
                    </a>
                    <!-- <span class="tooltip>All Date</span>"-->
                </li>
                <li>
                    <a href="#">
                        <i class='bx bxs-add-to-queue' ></i>
                        <span class="link">Add Event</span>
                    </a>
                    <!-- <span class="tooltip>All Date</span>"-->
                </li>
            </ul>
        </div>

        <!-- add Event -->
        <router-link :to="{ name: 'AddEvent' }">
            <div>
                <button class="button">+ Add Event</button>
            </div> 
        </router-link>
        <!-- /add Event -->
    </div>  
</template>

<style scoped>
.button {
  background-color: rgba(30, 38, 64, 1);
  border: none;
  color: rgba(243, 234, 192, 1);
  padding: 10px 20px;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  margin: 4px 2px;
  cursor: pointer;
  border-radius: 16px;
  position: fixed;
  bottom: 0%;
  right: 30%;
}
body{
position: relative;
min-height: 100vh;
width: 100%;
overflow: hidden;
}
.sidebar{
position: fixed;
top:0;
left: 0;
height: 100%;
width: 240px;
background: #1E2640;
}
.sidebar .logo_content .logo{
color:#fff;
display:flex;
height:50px;
width:100%;
align-items:center;
}
.logo_content  .logo .logo_name{
font-size:20px;
font-weight:400;    
}
.sidebar #btn{
position:absolute;
color:#fff;
left:90%;
top:6px;
font-size:20px;
height:50px;
width:50;
text-align: center;
}
</style>
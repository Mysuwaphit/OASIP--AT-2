<script setup>
import { ref, onBeforeMount } from 'vue'
import { useRouter } from 'vue-router'

defineProps({
  eventId: {
    type: Number,
    // require: true
  },
  userId: {
	type: Number,
	// require: true
  },
  userOrEvent: {
	type: Boolean,
	require: true
  }

}) 
let status = ref(0)
const token = `Bearer ${localStorage.getItem('accessToken')}`
const filterEvent = ref([])
const postRefreshToken = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/refresh`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'Authorization': token,
          'isRefreshToken': true
        }
      })
      if(res.status === 200){
        status.value = res.status
        const response = res.json()
        response.then(jsonRes => {
         const reToken = jsonRes.jwt
         localStorage.setItem('accessToken', reToken);
         console.log(reToken)
         console.log(localStorage.getItem('accessToken'))
         window.location.reload();
        })
      }else alert("Something went wrong! Please log in again.")
}
const eventList = ref([])
const getEventList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events`
  ,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const event = await res.json();
    eventList.value = event
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } else {
    console.log("No Scheduled Events");
  }
};

// const categoryList = ref([])
// const getCategory = async () => {
//   const res = await fetch(`${import.meta.env.VITE_BASE_URL}/eventCategories`)
//   if (res.status === 200) {
//     const List = await res.json(); 
//     categoryList.value= List
//   } else {
//     console.log("No Category");
//   }
// };

const userList = ref([])
const getUserList = async () => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users`,{
        method: 'GET',
        headers: {
          'content-type': 'application/json',
          'authorization': token
        }
  })
  if (res.status === 200) {
    const event = await res.json();
    userList.value = event
  } else if(res.status === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
  } else {
    console.log("No Scheduled Events");
  }
};

onBeforeMount(async () => {
  await getEventList();
//   await getCategory();
  await getUserList();
});
if(status.value === 401){
    console.log("Access token expired!!!!")
    postRefreshToken();
}

const appRouter = useRouter()
const goToHome = () => appRouter.push({ name: 'EventListing' })

const removeEvent = async (removeEventId) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/events/${removeEventId}`, {
    method: "DELETE",
    headers: {
          // 'content-type': 'application/json',
          'authorization': token
    }
  });
  if (res.status === 200) {
    eventList.value = eventList.value.filter((e) => e.id !== removeEventId);
	goToHome();
    console.log("delete  successfully");
  } else {
    console.log("error,cannot delete");
  }
};

const removeUser = async (removeUserId) => {
  const res = await fetch(`${import.meta.env.VITE_BASE_URL}/users/${removeUserId}`, {
    method: "DELETE",
    headers: {
          // 'content-type': 'application/json',
          'authorization': token
    }
  });
  if (res.status === 200) {
    eventList.value = eventList.value.filter((e) => e.id !== removeUserId);
	goToHome();
    console.log("delete successfully");
  } else {
    console.log("error,cannot delete");
  }
};

// const userOrEvent = ref(true);
// const checkUserorEvent = () => {
// 	if(userOrEvent.value === true){
// 		return removeEvent($route.params.eventId)
// 	}
// 	else{
// 		return removeUser($route.params.userId)
// 	}
// }

</script>
 
<template>

<!-- Modal HTML -->
<div id="myModal" class="modal fade">
	<div class="modal-dialog modal-confirm">
		<div class="modal-content">
			<div class="modal-header flex-column">
				<div class="icon-box">
					<i class="material-icons">&#xE5CD;</i>
				</div>						
				<h4 class="modal-title w-100">Are you sure?</h4>	
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
			</div>
			<div class="modal-body">
				<p>Do you really want to delete these? This process cannot be undone.</p>
			</div>
			<div class="modal-footer justify-content-center">
				<button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
				<button type="button" class="btn btn-secondary btn-danger" id="delete" data-dismiss="modal" @click="userOrEvent === true? removeUser($route.params.userId):removeEvent($route.params.eventId),userOrEvent != userOrEvent">DELETE</button>
			</div>
		</div>
	</div>
</div> 
</template>
 
<style scoped>
.modal-confirm {		
	color: #636363;
	width: 400px;
}
.modal-confirm .modal-content {
	padding: 20px;
	border-radius: 5px;
	border: none;
	text-align: center;
	font-size: 14px;
}
.modal-confirm .modal-header {
	border-bottom: none;   
	position: relative;
}
.modal-confirm h4 {
	text-align: center;
	font-size: 26px;
	margin: 30px 0 -10px;
}
.modal-confirm .close {
	position: absolute;
	top: -5px;
	right: -2px;
}
.modal-confirm .modal-body {
	color: #999;
}
.modal-confirm .modal-footer {
	border: none;
	text-align: center;		
	border-radius: 5px;
	font-size: 13px;
	padding: 10px 15px 25px;
}
.modal-confirm .modal-footer a {
	color: #999;
}		
.modal-confirm .icon-box {
	width: 80px;
	height: 80px;
	margin: 0 auto;
	border-radius: 50%;
	z-index: 9;
	text-align: center;
	border: 3px solid #f15e5e;
}
.modal-confirm .icon-box i {
	color: #f15e5e;
	font-size: 46px;
	display: inline-block;
	margin-top: 13px;
}
.modal-confirm .btn, .modal-confirm .btn:active {
	color: #fff;
	border-radius: 4px;
	background: #60c7c1;
	text-decoration: none;
	transition: all 0.4s;
	line-height: normal;
	min-width: 120px;
	border: none;
	min-height: 40px;
	border-radius: 3px;
	margin: 0 5px;
}
.modal-confirm .btn-secondary {
	background: #c1c1c1;
}
.modal-confirm .btn-secondary:hover, .modal-confirm .btn-secondary:focus {
	background: #a8a8a8;
}
.modal-confirm .btn-danger {
	background: #f15e5e;
}
.modal-confirm .btn-danger:hover, .modal-confirm .btn-danger:focus {
	background: #ee3535;
}
.trigger-btn {
	display: inline-block;
	margin: 100px auto;
}
</style>
console.log("this is script file written by manohar");
const toggleSidebar=()=>{
	
	if($(".sidebar").is(":visible")){
		
		$(".sidebar").css("display","none");
		$(".content").css("margin-left","0%");
	}
	else{
		$(".sidebar").css("display","block");
		$(".content").css("margin-left","20%");
	}
	
};

const search = ()=>{
	//console.log("searching.");
	let query = $("#search-input").val();
	
	if(query=='')
	{
		$(".search-result").hide();
	}
	else{
		//console.log(query);
		
	let url =`http://localhost:8080/search/${query}`;
	fetch(url).then(
		(res)=>{
			return res.json();
		}
		
	).then((data)=>{
		console.log(data);
		let text =`<div class="list-group">`;
		data.forEach((c)=>{
			text+=`<a href="/user/${c.cId}/contact" class="list-group-item list-group-item-action " > ${c.name} </a>`;
		});
		text+=`</div>`;
		$(".search-result").html(text);
		$(".search-result").show();
		
		
	});
	
	
	}
};
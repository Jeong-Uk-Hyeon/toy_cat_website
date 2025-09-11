<!-- Vue 의 **최상위 컴포넌트(root component) 
 보통 공통 레이아웃(헤더, 푸터 등)을 포함하며 헤더와 푸터는 각각 맨위 맨 아래 늘 안변하는 부분
<router-view/>로 라우터에 따라 페이지가 전환되도록 함-->

<template>
  <nav>
    <h1>스마트 반려묘 원격 케어ㆍ놀이 시스템</h1>
  </nav>
  <router-view/> <!-- router의 /에 해당하는 컴포넌트와 연결 -->
  <div>
    Made by 김민준, 정욱현
  </div>
</template>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

nav {
  padding: 30px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
}

nav a.router-link-exact-active {
  color: #42b983;
}
</style>

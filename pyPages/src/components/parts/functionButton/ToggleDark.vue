<script setup>
import {SwitchButton,} from '@element-plus/icons-vue'
import {useDark, useToggle} from '@vueuse/core'
import {watch} from 'vue';
import {useStore} from 'vuex'

const store = useStore();


const isDark = useDark()
updateStoreDark();
const toggleDark = useToggle(isDark);

watch(isDark, () => {
  updateStoreDark();
})

function updateStoreDark() {
  store.commit('updateIsDark', isDark);
}

</script>

<template>
  <el-button type="info" size="large" class="big-btn"
             @click="toggleDark()" :icon="SwitchButton"></el-button>
</template>

<style scoped>

</style>
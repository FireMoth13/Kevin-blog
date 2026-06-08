import {ref,watch} from 'vue'

const theme = ref(localStorage.getItem('theme') || 'night')

export function useTheme() {
    function toggleTheme() {
        theme.value=theme.value === 'night'?'day':'night'
    }

    watch(theme,(val)=>{
        document.documentElement.setAttribute('data-theme',val)
        localStorage.setItem('theme',val)
    },{immediate:true})

    return {theme,toggleTheme}
}
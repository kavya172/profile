import React from 'react'
import SideBar from './SideBar'
import MainContent from './MainContent'
import Header from './Header'

const Body = () => {
  return (
    <>
    
    <div className='profile-body'>  
       <Header/>
    <div style={{display:'flex', flexDirection:'row', width:'100%'}}>
       <MainContent/>
       <SideBar/>
    </div>
    </div>ß
    </>
  )
}


export default Body
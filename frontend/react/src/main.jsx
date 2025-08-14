import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import {ChakraProvider, createStandaloneToast} from '@chakra-ui/react'
const {ToastContainer}  = createStandaloneToast()

createRoot(document.getElementById('root')).render(
  <StrictMode>
      <ChakraProvider>
          <App />
          <ToastContainer />
      </ChakraProvider>

  </StrictMode>,
)

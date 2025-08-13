import SidebarWithHeader from "./components/shared/SideBar.jsx";
import {useEffect, useState} from "react";
import {getCustomers} from "./services/client.js";
import {
    Spinner,
    Text,
    Wrap,
    WrapItem
} from "@chakra-ui/react";

import CartWithImage from "./components/Cart.jsx";

function App() {
    const [customers, setCustomers] = useState([]);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true)

        setTimeout(() => {
            getCustomers().then((response) => {
                setCustomers(response.data);
            }).catch((error) => {
                console.log(error);
            }).finally(() => {
                setLoading(false);
            })
        }, 1000)
    }, [])

    if (loading) {
        return (
            <SidebarWithHeader>
                <Spinner
                    thickness='4px'
                    speed='0.65s'
                    emptyColor='gray.200'
                    color='blue.500'
                    size='xl'
                />
            </SidebarWithHeader>
        )
    }

    if (customers.length <= 0) {
        return (<SidebarWithHeader>
            <Text>
                No Customers available
            </Text>
        </SidebarWithHeader>)
    }

    return (
        <SidebarWithHeader>
            <Wrap spacing='30px' justify='center'>
                {customers.map((customer, index) => (
                    <WrapItem key={index}>
                        <CartWithImage
                            {...customer}
                        imageNumber={index}
                        />
                    </WrapItem>
                ))}
            </Wrap>
        </SidebarWithHeader>
    )
}

export default App

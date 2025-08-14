import {
    AlertDialog,
    AlertDialogBody,
    AlertDialogContent, AlertDialogFooter,
    AlertDialogHeader,
    AlertDialogOverlay,
    Button, useDisclosure
} from "@chakra-ui/react";

import {deleteCustomer} from "../services/client.js";
import {errorNotification, successNotification} from "../services/notification.js";
import React from "react";


const DeleteCustomer = ({fetchCustomers, customerId, name}) => {
    const { isOpen, onOpen, onClose } = useDisclosure()
    const cancelRef = React.useRef()

    return (
        <>
            <Button
                bg={'red.400'}
                color={'white'}
                rounded={'full'}
                _hover={{
                    transform: 'translateY(-2px)',
                    boxShadow: 'lg',
                }}
                _focus={{
                    bg: 'green.500'
                }}

                onClick={onOpen}
            >
                Delete

            </Button>

            <AlertDialog
                isOpen={isOpen}
                leastDestructiveRef={cancelRef}
                onClose={onClose}
            >
                <AlertDialogOverlay>
                    <AlertDialogContent>
                        <AlertDialogHeader fontSize='lg' fontWeight='bold'>
                            Delete Customer
                        </AlertDialogHeader>

                        <AlertDialogBody>
                            Are you sure you want to delete {name}? You can't undo this action afterwards.
                        </AlertDialogBody>

                        <AlertDialogFooter>
                            <Button ref={cancelRef} onClick={onClose}>
                                Cancel
                            </Button>
                            <Button colorScheme='red' onClick={() =>{
                                deleteCustomer(customerId).then(res => {
                                    console.log(res)
                                    successNotification(

                                        'Customer deleted.',
                                        `${name} was successfully deleted.`
                                    )
                                    fetchCustomers();
                                }).catch(err => {
                                    errorNotification(
                                        err.code,
                                        err.response.data.message,
                                    )
                                }).finally(
                                    onClose()
                                )
                            }} ml={3}>
                                Delete
                            </Button>
                        </AlertDialogFooter>
                    </AlertDialogContent>
                </AlertDialogOverlay>
            </AlertDialog>
        </>
    )
}

export default DeleteCustomer